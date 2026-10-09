package com.anurag.sms.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anurag.sms.dto.ImportReport;
import com.anurag.sms.entity.ActivityLog;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Student;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.service.ActivityLogService;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.utility.CsvHelper;
import com.anurag.sms.utility.CsvHelper.RowError;
import com.anurag.sms.utility.Pages;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.repository.FeeRepository;
import com.anurag.sms.repository.ResultRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FeeRepository feeRepository;
    private final ResultRepository resultRepository;
    private final CourseRepository courseRepository;

    // The same Bean Validation the Student form runs through @Valid
    private final Validator validator;
    private final ActivityLogService activityLogService;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            AttendanceRepository attendanceRepository,
            EnrollmentRepository enrollmentRepository,
            FeeRepository feeRepository,
            ResultRepository resultRepository,
            CourseRepository courseRepository,
            Validator validator,
            ActivityLogService activityLogService) {

        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.feeRepository = feeRepository;
        this.resultRepository = resultRepository;
        this.courseRepository = courseRepository;
        this.validator = validator;
        this.activityLogService = activityLogService;
    }

    @Override
    public List<Student> getRecentStudents() {

        return studentRepository.findTop5ByOrderByIdDesc();

    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    @Override
    public Student saveStudent(Student student) {

        // The form posts both new and edited students here
        boolean isNew = student.getId() == null;
        Student saved = studentRepository.save(student);

        activityLogService.record(isNew ? ActivityLog.CREATED : ActivityLog.UPDATED,
                "Student", fullName(saved));
        return saved;
    }

    @Override
    public Student updateStudent(Student student) {

        Student saved = studentRepository.save(student);

        activityLogService.record(ActivityLog.UPDATED, "Student", fullName(saved));
        return saved;
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {

        // Read the name first: afterwards there is nothing left to describe.
        // An unknown id deletes nothing, so it is not logged either.
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return;
        }
        String name = fullName(student);

        attendanceRepository.deleteByStudentId(id);

        enrollmentRepository.deleteByStudentId(id);

        feeRepository.deleteByStudentId(id);

        resultRepository.deleteByStudentId(id);

        studentRepository.deleteById(id);

        activityLogService.record(ActivityLog.DELETED, "Student", name);
    }

    private static String fullName(Student student) {
        return student.getFirstName() + " " + student.getLastName();
    }

    @Override
    public Page<Student> searchStudents(String keyword, int pageNo) {

        // Search used to return every match on one page (#11).
        if (keyword == null || keyword.isBlank()) {
            return studentRepository.findAll(Pages.of(pageNo));
        }

        String k = keyword.trim();
        return studentRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(
                        k, k, k, k, Pages.of(pageNo));
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public long getTotalStudents() {
        return studentRepository.count();
    }

    @Override
    public long getMaleStudents() {
        return studentRepository.countByGender("Male");
    }

    @Override
    public long getFemaleStudents() {
        return studentRepository.countByGender("Female");
    }

    @Override
    public ImportReport importStudents(InputStream csv) throws IOException {

        CsvHelper.StudentParseResult parsed = CsvHelper.parseStudents(csv);
        List<RowError> skipped = new ArrayList<>(parsed.errors());

        // Course names as the form's dropdown offers them, matched ignoring
        // case; the student gets the stored spelling
        Map<String, String> courses = new HashMap<>();
        for (Course course : courseRepository.findAll()) {
            courses.putIfAbsent(key(course.getCourseName()), course.getCourseName());
        }

        Set<String> emailsInFile = new HashSet<>();
        int imported = 0;

        for (CsvHelper.ParsedStudent row : parsed.students()) {

            Student student = row.student();
            String problem = problemWith(student, courses, emailsInFile);

            if (problem != null) {
                skipped.add(new RowError(row.row(), problem));
                continue;
            }

            try {
                studentRepository.save(student);
                emailsInFile.add(key(student.getEmail()));
                imported++;
            } catch (DataIntegrityViolationException e) {
                // Only if another request saved the same email in between
                skipped.add(new RowError(row.row(), "email " + student.getEmail() + " exists"));
            }
        }

        // One line for the whole file rather than one per student
        if (imported > 0) {
            activityLogService.record(ActivityLog.IMPORTED, "Student",
                    imported + (imported == 1 ? " student" : " students") + " from a CSV file");
        }

        skipped.sort(Comparator.comparingInt(RowError::row));
        return new ImportReport(imported, skipped);
    }

    // The first reason this row cannot be imported, or null if it can. Sets
    // the student's course to the stored course name.
    private String problemWith(Student student, Map<String, String> courses, Set<String> emailsInFile) {

        // Bean Validation messages, in the file's column order
        List<String> invalid = validator.validate(student).stream()
                .sorted(Comparator.comparingInt(v -> columnOf(v.getPropertyPath().toString())))
                .map(ConstraintViolation::getMessage)
                .toList();

        if (!invalid.isEmpty()) {
            return String.join("; ", invalid);
        }

        if (emailsInFile.contains(key(student.getEmail()))) {
            return "email " + student.getEmail() + " appears earlier in this file";
        }

        if (studentRepository.existsByEmail(student.getEmail())) {
            return "email " + student.getEmail() + " exists";
        }

        String course = courses.get(key(student.getCourse()));
        if (course == null) {
            return "course \"" + student.getCourse() + "\" not found";
        }
        student.setCourse(course);

        return null;
    }

    private static int columnOf(String property) {
        int i = CsvHelper.STUDENT_HEADER.indexOf(property);
        return i < 0 ? Integer.MAX_VALUE : i;
    }

    private static String key(String text) {
        return text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
    }
}
