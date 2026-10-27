package com.anurag.sms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.TestData;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Result;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.ExamRepository;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.SubjectRepository;

/**
 * One result per student and exam (#41). A second one used to be saved,
 * and the marksheet then counted that exam twice in the totals and the
 * overall grade.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ResultDuplicateTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CourseRepository courses;

    @Autowired
    private StudentRepository students;

    @Autowired
    private SubjectRepository subjects;

    @Autowired
    private ExamRepository exams;

    @Autowired
    private ResultRepository results;

    private Student student;
    private Exam exam;
    private Result first;

    @BeforeEach
    void oneResult() {
        Course course = courses.save(TestData.course("BSC01", "BSc Physics"));
        Subject subject = subjects.save(TestData.subject("PHY01", "Mechanics"));
        student = students.save(TestData.student("Ira", "Menon", course));
        exam = exams.save(TestData.exam("Mid Semester", subject, LocalDate.of(2026, 9, 20)));
        first = results.save(TestData.result(student, exam, 70));
    }

    @Test
    void aSecondResultForTheSameExamShowsAMessageAndSavesNothing() throws Exception {
        mvc.perform(post("/result/save").with(user("teacher").roles("TEACHER")).with(csrf())
                        .param("student", student.getId().toString())
                        .param("exam", exam.getId().toString())
                        .param("obtainedMarks", "35"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("already has a result for this exam")));

        assertThat(results.findByStudentIdOrderByExamExamDateAsc(student.getId())).hasSize(1);
    }

    @Test
    void editingAResultMayKeepItsOwnStudentAndExam() throws Exception {
        mvc.perform(post("/result/update/" + first.getId()).with(user("teacher").roles("TEACHER")).with(csrf())
                        .param("id", first.getId().toString())
                        .param("student", student.getId().toString())
                        .param("exam", exam.getId().toString())
                        .param("obtainedMarks", "82"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/result"));

        assertThat(results.findById(first.getId()).orElseThrow().getObtainedMarks()).isEqualTo(82);
    }

    // The form checks first; the unique constraint (V7) stops anything that
    // gets past it, such as two submissions at the same moment
    @Test
    void theDatabaseRefusesADuplicateToo() {
        assertThatThrownBy(() -> results.saveAndFlush(TestData.result(student, exam, 35)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
