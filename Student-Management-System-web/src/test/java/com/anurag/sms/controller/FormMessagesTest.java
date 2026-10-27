package com.anurag.sms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.TestData;
import com.anurag.sms.entity.Attendance;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.SubjectRepository;

/**
 * Form messages from Stage G: a friendly message for a value of the wrong
 * type (#33), and the duplicate check on the single attendance form (#35).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(roles = "ADMIN")
class FormMessagesTest {

    private static final LocalDate OCT_1 = LocalDate.of(2026, 10, 1);

    @Autowired private MockMvc mvc;
    @Autowired private CourseRepository courses;
    @Autowired private StudentRepository students;
    @Autowired private SubjectRepository subjects;
    @Autowired private AttendanceRepository attendance;

    private Student student;
    private Subject subject;
    private Attendance existing;

    @BeforeEach
    void data() {
        Course course = courses.save(TestData.course("BSC-PHY", "B.Sc Physics"));
        student = students.save(TestData.student("Kabir", "Mehta", course));
        subject = subjects.save(TestData.subject("MATH01", "Mathematics"));
        existing = attendance.save(TestData.attendance(student, subject, OCT_1, "Present"));
    }

    // #33: only reachable by bypassing the browser's number box
    @Test
    void lettersInANumberFieldGetAPlainMessage() throws Exception {
        mvc.perform(post("/exam/save").with(csrf())
                        .param("examName", "Unit test")
                        .param("subject", subject.getId().toString())
                        .param("examDate", "2026-12-01")
                        .param("totalMarks", "abc")
                        .param("passingMarks", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please enter a whole number.")))
                .andExpect(content().string(not(containsString("Failed to convert"))));
    }

    // #45: results are capped at 1000 marks (Result and a CHECK in the
    // database), so an exam out of more could never get its top marks
    @Test
    void anExamTotalOverTheMarksCapIsRefusedOnTheForm() throws Exception {
        mvc.perform(post("/exam/save").with(csrf())
                        .param("examName", "Final")
                        .param("subject", subject.getId().toString())
                        .param("examDate", "2026-12-01")
                        .param("totalMarks", "1500")
                        .param("passingMarks", "600"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Total marks cannot be more than 1000")));
    }

    private ResultActions postAttendance(String url, LocalDate date) throws Exception {
        return mvc.perform(post(url).with(csrf())
                .param("student", student.getId().toString())
                .param("subject", subject.getId().toString())
                .param("attendanceDate", date.toString())
                .param("status", "Absent"));
    }

    // #35: the bulk form already refused this; the single form now does too
    @Test
    void aSecondRecordForTheSameStudentSubjectAndDayIsRefused() throws Exception {
        postAttendance("/attendance/save", OCT_1)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "This student already has attendance for this subject on this date.")));

        assertThat(attendance.count()).isEqualTo(1);
    }

    @Test
    void anotherDayIsSaved() throws Exception {
        postAttendance("/attendance/save", OCT_1.plusDays(1))
                .andExpect(redirectedUrl("/attendance"));

        assertThat(attendance.count()).isEqualTo(2);
    }

    // An edit may keep its own student, subject and date
    @Test
    void editingARecordKeepsItsOwnValues() throws Exception {
        postAttendance("/attendance/update/" + existing.getId(), OCT_1)
                .andExpect(redirectedUrl("/attendance"));

        assertThat(attendance.findById(existing.getId())).get()
                .extracting(Attendance::getStatus).isEqualTo("Absent");
    }

    @Test
    void anEditCannotMoveARecordOntoAnotherOne() throws Exception {
        Attendance other = attendance.save(TestData.attendance(student, subject, OCT_1.plusDays(1), "Present"));

        postAttendance("/attendance/update/" + other.getId(), OCT_1)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "This student already has attendance for this subject on this date.")));
    }
}
