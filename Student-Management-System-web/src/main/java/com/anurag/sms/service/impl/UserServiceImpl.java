package com.anurag.sms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anurag.sms.dto.UserRegistrationDto;
import com.anurag.sms.entity.ActivityLog;
import com.anurag.sms.entity.User;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.UserRepository;
import com.anurag.sms.service.ActivityLogService;
import com.anurag.sms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogService activityLogService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activityLogService = activityLogService;
    }

    @Override
    public User registerUser(UserRegistrationDto registrationDto) {

        // Check duplicate username
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }

        // Check duplicate email
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        // Check password confirmation
        if (!registrationDto.getPassword()
                .equals(registrationDto.getConfirmPassword())) {

            throw new RuntimeException("Passwords do not match.");
        }

        User user = new User();

        user.setFullName(registrationDto.getFullName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());

        // Encrypt password
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        // Default Role
        user.setRole("ROLE_STUDENT");

        user.setEnabled(true);

        return userRepository.save(user);
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
        User saved = userRepository.save(user);

        activityLogService.record(ActivityLog.UPDATED, "User", "Role of " + user.getUsername() + ": "
                + before.replace("ROLE_", "") + " to " + role.replace("ROLE_", ""));
        return saved;
    }

    private User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}