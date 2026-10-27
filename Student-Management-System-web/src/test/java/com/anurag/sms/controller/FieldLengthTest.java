package com.anurag.sms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.TestData;
import com.anurag.sms.entity.ActivityLog;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.entity.User;
import com.anurag.sms.repository.ActivityLogRepository;
import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.SubjectRepository;
import com.anurag.sms.repository.UserRepository;
import com.anurag.sms.utility.FieldLimits;

/**
 * Text longer than its column (#39), and audit lines longer than theirs
 * (#40). Every text column is VARCHAR(255); a longer value used to fail in
 * the database, so the form was lost behind the 409 page, which says
 * nothing was changed.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FieldLengthTest {

    private static final String TOO_LONG = "x".repeat(FieldLimits.TEXT + 1);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CourseRepository courses;

    @Autowired
    private StudentRepository students;

    @Autowired
    private SubjectRepository subjects;

    @Autowired
    private AttendanceRepository attendance;

    @Autowired
    private UserRepository users;

    @Autowired
    private ActivityLogRepository activity;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    void aCourseNameOverTheLimitShowsAMessageOnTheForm() throws Exception {
        mvc.perform(post("/course").with(user("admin").roles("ADMIN")).with(csrf())
                        .param("courseCode", "LONG01")
                        .param("courseName", TOO_LONG)
                        .param("duration", "3 years")
                        .param("fees", "40000"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Course Name" + FieldLimits.TEXT_TOO_LONG)));

        assertThat(courses.existsByCourseCode("LONG01")).isFalse();
    }

    @Test
    void anAddressOverTheLimitShowsAMessageOnTheStudentForm() throws Exception {
        Course course = courses.save(TestData.course("BSC01", "BSc Physics"));

        mvc.perform(multipart("/student")
                        .file(new MockMultipartFile("photoFile", "", "application/octet-stream", new byte[0]))
                        .with(user("admin").roles("ADMIN")).with(csrf())
                        .param("firstName", "Ira")
                        .param("lastName", "Menon")
                        .param("email", "ira.menon@example.com")
                        .param("phone", "9876500001")
                        .param("gender", "Female")
                        .param("course", course.getId().toString())
                        .param("dateOfBirth", "2005-04-01")
                        .param("address", TOO_LONG))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Address" + FieldLimits.TEXT_TOO_LONG)));

        assertThat(students.existsByEmail("ira.menon@example.com")).isFalse();
    }

    @Test
    void aLongRemarkShowsAMessageOnTheBulkForm() throws Exception {
        Course course = courses.save(TestData.course("BSC01", "BSc Physics"));
        Student student = students.save(TestData.student("Ira", "Menon", course));
        Subject subject = subjects.save(TestData.subject("PHY01", "Mechanics"));

        mvc.perform(post("/attendance/bulk").with(user("teacher").roles("TEACHER")).with(csrf())
                        .param("subjectId", subject.getId().toString())
                        .param("date", "2026-10-05")
                        .param("entries[0].studentId", student.getId().toString())
                        .param("entries[0].studentName", "Ira Menon")
                        .param("entries[0].status", "Present")
                        .param("entries[0].remarks", TOO_LONG))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Remarks" + FieldLimits.TEXT_TOO_LONG)));

        assertThat(attendance.existsByStudentIdAndSubjectIdAndAttendanceDate(
                student.getId(), subject.getId(), LocalDate.of(2026, 10, 5))).isFalse();
    }

    @Test
    void aUsernameOverTheLimitIsRefusedAtRegistration() throws Exception {
        String username = "u".repeat(FieldLimits.USERNAME + 1);

        mvc.perform(post("/register").with(csrf())
                        .param("fullName", "Neha Rao")
                        .param("username", username)
                        .param("email", "neha.rao@example.com")
                        .param("password", "abcdefgh")
                        .param("confirmPassword", "abcdefgh"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(FieldLimits.USERNAME_TOO_LONG)));

        assertThat(users.existsByUsername(username)).isFalse();
    }

    // #40: the password was saved, then the audit line overflowed
    // activity_log.username (100); the user saw "Nothing was changed".
    // Accounts made before the username limit can still be this long.
    @Test
    void anAccountWithAVeryLongUsernameCanChangeItsPassword() throws Exception {
        String username = "u".repeat(120);
        User account = new User();
        account.setFullName("Neha Rao");
        account.setUsername(username);
        account.setEmail("neha.rao@example.com");
        account.setPassword(encoder.encode("oldpass123"));
        account.setRole("ROLE_STUDENT");
        account.setEnabled(true);
        users.save(account);

        mvc.perform(post("/profile/password").with(user(username).roles("STUDENT")).with(csrf())
                        .param("currentPassword", "oldpass123")
                        .param("newPassword", "newpass123")
                        .param("confirmPassword", "newpass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        assertThat(encoder.matches("newpass123", users.findByUsername(username).orElseThrow().getPassword()))
                .isTrue();

        ActivityLog line = activity.findTop10ByOrderByCreatedAtDescIdDesc().get(0);
        assertThat(line.getUsername()).hasSizeLessThanOrEqualTo(ActivityLog.USERNAME_LENGTH);
        assertThat(line.getDescription()).hasSizeLessThanOrEqualTo(ActivityLog.DESCRIPTION_LENGTH);
    }

    // The cut keeps the start of the line and marks that it was shortened
    @Test
    void anAuditLineLongerThanItsColumnIsShortened() {
        ActivityLog line = new ActivityLog(ActivityLog.UPDATED, "Student", "d".repeat(400), "u".repeat(150));

        assertThat(line.getDescription()).hasSize(ActivityLog.DESCRIPTION_LENGTH).startsWith("ddd").endsWith("…");
        assertThat(line.getUsername()).hasSize(ActivityLog.USERNAME_LENGTH).endsWith("…");
        assertThat(new ActivityLog(ActivityLog.UPDATED, "Student", "Ira Menon", "admin").getDescription())
                .isEqualTo("Ira Menon");
    }
}
