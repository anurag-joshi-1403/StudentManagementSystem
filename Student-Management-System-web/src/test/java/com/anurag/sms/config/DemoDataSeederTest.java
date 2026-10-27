package com.anurag.sms.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.anurag.sms.dto.AttendanceSummary;
import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.repository.ExamRepository;
import com.anurag.sms.repository.FeeRepository;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.SubjectRepository;
import com.anurag.sms.repository.TeacherRepository;
import com.anurag.sms.repository.UserRepository;

/**
 * G12: the demo profile fills an empty database at startup. Its own H2
 * database, since the other tests share one and expect it empty.
 */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:demo;MODE=MySQL;DB_CLOSE_DELAY=-1")
@ActiveProfiles("demo")
class DemoDataSeederTest {

    @Autowired private DemoDataSeeder seeder;
    @Autowired private CourseRepository courses;
    @Autowired private SubjectRepository subjects;
    @Autowired private TeacherRepository teachers;
    @Autowired private StudentRepository students;
    @Autowired private EnrollmentRepository enrollments;
    @Autowired private AttendanceRepository attendance;
    @Autowired private ExamRepository exams;
    @Autowired private ResultRepository results;
    @Autowired private FeeRepository fees;
    @Autowired private UserRepository users;

    @Test
    void fillsAnEmptyDatabaseWithLinkedRecords() {
        assertThat(courses.count()).isEqualTo(3);
        assertThat(subjects.count()).isEqualTo(8);
        assertThat(teachers.count()).isEqualTo(5);
        assertThat(students.count()).isEqualTo(30);

        // Every student takes each subject of their course: 12 x 4 + 10 x 3 + 8 x 3
        assertThat(enrollments.count()).isEqualTo(102);
        // Six weeks of weekdays, whatever day the test runs on
        assertThat(attendance.count()).isEqualTo(102 * 30);
        assertThat(results.count()).isEqualTo(102);
        assertThat(fees.count()).isEqualTo(60);
    }

    // Each notification and report has something to show
    @Test
    void includesSomethingForEveryAlert() {
        assertThat(attendance.findBelowMinimum(AttendanceSummary.MINIMUM_PERCENT)).isNotEmpty();
        assertThat(fees.countByPaymentStatusNotAndDueDateBefore("Paid", LocalDate.now())).isPositive();
        assertThat(exams.findAll()).anyMatch(exam -> !exam.getExamDate().isBefore(LocalDate.now())
                && exam.getExamDate().isBefore(LocalDate.now().plusDays(7)));
        assertThat(results.findAll()).anyMatch(result -> "Fail".equals(result.getResultStatus()));
    }

    @Test
    void createsOneLinkedAccountPerRole() {
        assertThat(users.findByUsername("demo_admin")).get()
                .satisfies(u -> assertThat(u.getRole()).isEqualTo("ROLE_ADMIN"));
        assertThat(users.findStudentIdByUsername("demo_student")).isPresent();
        assertThat(users.findTeacherIdByUsername("demo_teacher")).isPresent();
    }

    // A second start must not add a second college
    @Test
    void doesNothingWhenRecordsExist() {
        seeder.run(null);

        assertThat(students.count()).isEqualTo(30);
        assertThat(users.count()).isEqualTo(3);
    }
}
