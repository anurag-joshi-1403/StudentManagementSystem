package com.anurag.sms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anurag.sms.dto.UserRegistrationDto;
import com.anurag.sms.entity.ActivityLog;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Teacher;
import com.anurag.sms.entity.User;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.TeacherRepository;
import com.anurag.sms.repository.UserRepository;
import com.anurag.sms.service.ActivityLogService;
import com.anurag.sms.service.UserService;
import com.anurag.sms.service.UserSessionService;

@Service
public class UserServiceImpl implements UserService {

    private static final String STUDENT_ROLE = "ROLE_STUDENT";
    private static final String TEACHER_ROLE = "ROLE_TEACHER";

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogService activityLogService;
    private final UserSessionService userSessionService;

    public UserServiceImpl(UserRepository userRepository,
                           StudentRepository studentRepository,
                           TeacherRepository teacherRepository,
                           PasswordEncoder passwordEncoder,
                           ActivityLogService activityLogService,
                           UserSessionService userSessionService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.activityLogService = activityLogService;
        this.userSessionService = userSessionService;
    }

    @Override
    public User registerUser(UserRegistrationDto registrationDto) {

        // Check duplicate username
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        // Check duplicate email
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        // Check password confirmation
        if (!registrationDto.getPassword()
                .equals(registrationDto.getConfirmPassword())) {

            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = new User();

        user.setFullName(registrationDto.getFullName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());

        // Encrypt password
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        // Default Role. An admin links the account to its student record on
        // the Users page (G10); matching on email would let anyone who knows
        // a student's address claim their records, until emails are verified.
        user.setRole(STUDENT_ROLE);

        user.setEnabled(true);

        // Another registration can take the username or email between the
        // checks above and this save; the unique index then refuses it.
        // The database's own message includes the SQL, so it never reaches
        // the page (#43).
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(ALREADY_REGISTERED);
        }
    }

    @Override
    public User findByUsername(String username) {

        Optional<User> user = userRepository.findByUsername(username);

        return user.orElse(null);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("No account for signed-in user " + username));
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {

        User user = getByUsername(username);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException(WRONG_CURRENT_PASSWORD);
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("The new password must be different from the current one.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        activityLogService.record(ActivityLog.UPDATED, "User", username + " changed their password");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtAscIdAsc();
    }

    @Override
    public User setEnabled(Long userId, boolean enabled, String actingUsername) {

        User user = getById(userId);

        if (user.getUsername().equals(actingUsername)) {
            throw new IllegalArgumentException("You cannot disable your own account.");
        }
        if (user.isEnabled() == enabled) {
            return user;
        }

        user.setEnabled(enabled);
        User saved = userRepository.save(user);

        // A disabled account is signed out now, not when its session ends (#37)
        if (!enabled) {
            userSessionService.endSessions(user.getUsername());
        }

        activityLogService.record(ActivityLog.UPDATED, "User",
                (enabled ? "Enabled" : "Disabled") + " the account " + user.getUsername());
        return saved;
    }

    @Override
    public User changeRole(Long userId, String role, String actingUsername) {

        if (!ROLES.contains(role)) {
            throw new IllegalArgumentException("Unknown role: " + role);
        }

        User user = getById(userId);

        if (user.getUsername().equals(actingUsername)) {
            throw new IllegalArgumentException("You cannot change your own role.");
        }
        if (user.getRole().equals(role)) {
            return user;
        }

        String before = user.getRole();
        user.setRole(role);

        // A link follows the role (G10): a student record only for a STUDENT
        // account, a teacher record only for a TEACHER account
        if (!STUDENT_ROLE.equals(role)) {
            user.setStudent(null);
        }
        if (!TEACHER_ROLE.equals(role)) {
            user.setTeacher(null);
        }

        User saved = userRepository.save(user);

        // A session keeps the role it signed in with, so sign the account
        // out; the new role applies from the next page they open (#37)
        userSessionService.endSessions(user.getUsername());

        activityLogService.record(ActivityLog.UPDATED, "User", "Role of " + user.getUsername() + ": "
                + before.replace("ROLE_", "") + " to " + role.replace("ROLE_", ""));
        return saved;
    }

    @Override
    public User linkStudent(Long userId, Long studentId) {

        User user = getById(userId);
        if (!STUDENT_ROLE.equals(user.getRole())) {
            throw new IllegalArgumentException("Only a STUDENT account can be linked to a student.");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
        String name = student.getFirstName() + " " + student.getLastName();

        userRepository.findByStudentId(studentId)
                .filter(other -> !other.getId().equals(userId))
                .ifPresent(other -> {
                    throw new IllegalArgumentException(
                            name + " is already linked to the account " + other.getUsername() + ".");
                });

        user.setStudent(student);
        user.setTeacher(null);
        User saved = userRepository.save(user);

        activityLogService.record(ActivityLog.UPDATED, "User",
                "Linked the account " + user.getUsername() + " to the student " + name);
        return saved;
    }

    @Override
    public User linkTeacher(Long userId, Long teacherId) {

        User user = getById(userId);
        if (!TEACHER_ROLE.equals(user.getRole())) {
            throw new IllegalArgumentException("Only a TEACHER account can be linked to a teacher.");
        }

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", teacherId));
        String name = teacher.getFirstName() + " " + teacher.getLastName();

        userRepository.findByTeacherId(teacherId)
                .filter(other -> !other.getId().equals(userId))
                .ifPresent(other -> {
                    throw new IllegalArgumentException(
                            name + " is already linked to the account " + other.getUsername() + ".");
                });

        user.setTeacher(teacher);
        user.setStudent(null);
        User saved = userRepository.save(user);

        activityLogService.record(ActivityLog.UPDATED, "User",
                "Linked the account " + user.getUsername() + " to the teacher " + name);
        return saved;
    }

    @Override
    public User unlink(Long userId) {

        User user = getById(userId);
        if (user.getStudent() == null && user.getTeacher() == null) {
            return user;
        }

        user.setStudent(null);
        user.setTeacher(null);
        User saved = userRepository.save(user);

        activityLogService.record(ActivityLog.UPDATED, "User", "Unlinked the account " + user.getUsername());
        return saved;
    }

    private User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}