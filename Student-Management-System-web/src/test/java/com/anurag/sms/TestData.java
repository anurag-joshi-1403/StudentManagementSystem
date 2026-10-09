package com.anurag.sms;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.anurag.sms.entity.Attendance;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Enrollment;
import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Fee;
import com.anurag.sms.entity.Result;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;

/**
 * Valid, unsaved entities for the tests. Every field the entity's Bean
 * Validation requires is filled, since Hibernate validates on insert.
 * Made-up people on @example.com only.
 */
public final class TestData {

    private TestData() {
    }

    public static Student student(String first, String last) {
        Student s = new Student();
        s.setFirstName(first);
        s.setLastName(last);
        s.setEmail(first.toLowerCase() + "." + last.toLowerCase() + "@example.com");
        s.setPhone("9876500000");
        s.setGender("Other");
        s.setCourse("B.Sc Physics");
        s.setDateOfBirth(LocalDate.of(2005, 1, 1));
        s.setAddress("1 Test Road, Pune");
        return s;
    }

    public static Subject subject(String code, String name) {
        return new Subject(null, code, name, "1", 4, "SOE");
    }

    public static Course course(String code, String name) {
        Course c = new Course();
        c.setCourseCode(code);
        c.setCourseName(name);
        c.setDuration("3 years");
        c.setFees(40000.0);
        return c;
    }

    public static Exam exam(String name, Subject subject, LocalDate date) {
        return new Exam(null, name, subject, date, 100, 33);
    }

    public static Attendance attendance(Student student, Subject subject, LocalDate date, String status) {
        return new Attendance(null, student, subject, date, status, null);
    }

    public static Enrollment enrollment(Student student, Course course, Subject subject) {
        Enrollment e = new Enrollment();
        e.setStudent(student);
        e.setCourse(course);
        e.setSubject(subject);
        e.setEnrollmentDate(LocalDate.of(2026, 8, 1));
        return e;
    }

    public static Fee fee(Student student) {
        return new Fee(null, student, "Tuition", new BigDecimal("60000.00"),
                LocalDate.of(2026, 9, 30), null, "Pending", null);
    }

    public static Result result(Student student, Exam exam, int marks) {
        return new Result(null, student, exam, marks, "B", "Pass");
    }
}
