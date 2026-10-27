package com.anurag.sms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.TestData;
import com.anurag.sms.entity.Attendance;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Enrollment;
import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Fee;
import com.anurag.sms.entity.Result;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.entity.Teacher;
import com.anurag.sms.entity.User;
import com.anurag.sms.repository.ActivityLogRepository;
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

import jakarta.persistence.EntityManager;

/**
 * F16: deleting a record that others point at (#25). No entity cascades,
 * so each service deletes the children itself; a missed child would make
 * the parent's DELETE break the foreign key. H2 enforces foreign keys like
 * MySQL does.
 *
 * Every test flushes after the delete. Without that the DELETE statements
 * would never run, because the test transaction is rolled back, and a
 * foreign-key error would go unnoticed.
 */
@SpringBootTest
@Transactional
class DeleteCascadeTest {

    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;
    @Autowired private SubjectService subjectService;
    @Autowired private ExamService examService;
    @Autowired private TeacherService teacherService;
    @Autowired private FeeService feeService;
    @Autowired private ResultService resultService;

    @Autowired private StudentRepository students;
    @Autowired private CourseRepository courses;
    @Autowired private SubjectRepository subjects;
    @Autowired private ExamRepository exams;
    @Autowired private AttendanceRepository attendance;
    @Autowired private EnrollmentRepository enrollments;
    @Autowired private FeeRepository fees;
    @Autowired private ResultRepository results;
    @Autowired private ActivityLogRepository activityLog;
    @Autowired private TeacherRepository teachers;
    @Autowired private UserRepository users;

    @Autowired private EntityManager em;

    private Student student;
    private Course course;
    private Subject subject;
    private Exam exam;
    private Attendance attendanceRow;
    private Enrollment enrollment;
    private Fee fee;
    private Result result;

    // One of everything, all linked: the student belongs to the course (G8),
    // which teaches the subject (G9); the student is enrolled in it, has
    // attendance and a fee, and a result in the exam
    @BeforeEach
    void linkedRecords() {
        subject = subjects.save(TestData.subject("MATH01", "Mathematics"));
        Course physics = TestData.course("BSC-PHY", "B.Sc Physics");
        physics.getSubjects().add(subject);
        course = courses.save(physics);
        student = students.save(TestData.student("Kabir", "Mehta", course));
        exam = exams.save(TestData.exam("Mid sem", subject, LocalDate.of(2026, 7, 31)));
        attendanceRow = attendance.save(TestData.attendance(student, subject, LocalDate.of(2026, 10, 1), "Present"));
        enrollment = enrollments.save(TestData.enrollment(student, course, subject));
        fee = fees.save(TestData.fee(student));
        result = results.save(TestData.result(student, exam, 78));

        em.flush();
        em.clear();
    }

    // Run the DELETEs now, then forget cached entities so the checks
    // below read the database
    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    @Test
    void deletingAStudentRemovesTheirAttendanceEnrollmentsFeesAndResults() {
        studentService.deleteStudent(student.getId());
        flushAndClear();

        assertThat(students.existsById(student.getId())).isFalse();
        assertThat(attendance.existsById(attendanceRow.getId())).isFalse();
        assertThat(enrollments.existsById(enrollment.getId())).isFalse();
        assertThat(fees.existsById(fee.getId())).isFalse();
        assertThat(results.existsById(result.getId())).isFalse();

        // Records that only pointed the other way stay
        assertThat(courses.existsById(course.getId())).isTrue();
        assertThat(exams.existsById(exam.getId())).isTrue();
    }

    // G8: every student belongs to a course, so a course with students stays
    @Test
    void aCourseWithStudentsIsNotDeleted() {
        assertThatThrownBy(() -> courseService.deleteCourse(course.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("still has 1 student");
        flushAndClear();

        assertThat(courses.existsById(course.getId())).isTrue();
        assertThat(enrollments.existsById(enrollment.getId())).isTrue();
    }

    // A course nobody belongs to goes, with its enrollments and its
    // course_subjects rows
    @Test
    void deletingACourseRemovesItsEnrollmentsAndCurriculum() {
        Course elective = TestData.course("BSC-MATH", "B.Sc Mathematics");
        elective.getSubjects().add(subject);
        elective = courses.save(elective);
        Enrollment electiveEnrollment = enrollments.save(TestData.enrollment(student, elective, subject));
        flushAndClear();

        courseService.deleteCourse(elective.getId());
        flushAndClear();

        assertThat(courses.existsById(elective.getId())).isFalse();
        assertThat(enrollments.existsById(electiveEnrollment.getId())).isFalse();
        assertThat(subjects.existsById(subject.getId())).isTrue();
        assertThat(students.existsById(student.getId())).isTrue();
    }

    @Test
    void deletingAnExamRemovesItsResults() {
        examService.deleteExam(exam.getId());
        flushAndClear();

        assertThat(exams.existsById(exam.getId())).isFalse();
        assertThat(results.existsById(result.getId())).isFalse();
        assertThat(subjects.existsById(subject.getId())).isTrue();
    }

    // Spring Data's deleteById ignores an unknown id; the activity log must
    // not then record a deletion that never happened
    @Test
    void deletingAnUnknownIdChangesNothingAndLogsNothing() {
        long logRows = activityLog.count();

        studentService.deleteStudent(99999L);
        teacherService.deleteTeacher(99999L);
        feeService.deleteFee(99999L);
        resultService.deleteResult(99999L);
        flushAndClear();

        assertThat(activityLog.count()).isEqualTo(logRows);
        assertThat(students.count()).isEqualTo(1);
        assertThat(fees.count()).isEqualTo(1);
        assertThat(results.count()).isEqualTo(1);
    }

    @Test
    void deletingASubjectRemovesItsAttendanceEnrollmentsExamsAndTheirResults() {
        subjectService.deleteSubject(subject.getId());
        flushAndClear();

        assertThat(subjects.existsById(subject.getId())).isFalse();
        assertThat(attendance.existsById(attendanceRow.getId())).isFalse();
        assertThat(enrollments.existsById(enrollment.getId())).isFalse();
        assertThat(exams.existsById(exam.getId())).isFalse();
        assertThat(results.existsById(result.getId())).isFalse();
        assertThat(students.existsById(student.getId())).isTrue();

        // G9: and out of the course's curriculum
        assertThat(courses.findById(course.getId()).orElseThrow().getSubjects()).isEmpty();
    }

    private User account(String username, String role) {
        User u = new User();
        u.setFullName(username);
        u.setUsername(username);
        u.setEmail(username + "@example.com");
        u.setPassword("not-a-real-hash");
        u.setRole(role);
        return u;
    }

    // G10: the account stays; only its link goes
    @Test
    void deletingAStudentKeepsTheirAccountButDropsTheLink() {
        User account = account("kabir01", "ROLE_STUDENT");
        account.setStudent(student);
        account = users.save(account);
        flushAndClear();

        studentService.deleteStudent(student.getId());
        flushAndClear();

        assertThat(students.existsById(student.getId())).isFalse();
        assertThat(users.findById(account.getId()).orElseThrow().getStudent()).isNull();
    }

    @Test
    void deletingATeacherKeepsTheirAccountButDropsTheLink() {
        Teacher teacher = teachers.save(new Teacher(null, "Meera", "Iyer", "meera.iyer@example.com",
                "9876500020", "Physics", "PhD", "Female", null));
        User account = account("meera01", "ROLE_TEACHER");
        account.setTeacher(teacher);
        account = users.save(account);
        flushAndClear();

        teacherService.deleteTeacher(teacher.getId());
        flushAndClear();

        assertThat(teachers.existsById(teacher.getId())).isFalse();
        assertThat(users.findById(account.getId()).orElseThrow().getTeacher()).isNull();
    }
}
