package com.anurag.sms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.SubjectRepository;

import jakarta.persistence.EntityManager;

/**
 * G8 and G9: a student's course is a real record, and each course has a
 * curriculum that enrollments must follow.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(roles = "ADMIN")
class CurriculumTest {

    @Autowired private MockMvc mvc;
    @Autowired private CourseRepository courses;
    @Autowired private SubjectRepository subjects;
    @Autowired private StudentRepository students;
    @Autowired private EnrollmentRepository enrollments;
    @Autowired private EntityManager em;

    private Course physics;
    private Subject maths;
    private Subject chemistry;
    private Student student;

    // Physics teaches Mathematics but not Chemistry
    @BeforeEach
    void data() {
        maths = subjects.save(TestData.subject("MATH01", "Mathematics"));
        chemistry = subjects.save(TestData.subject("CHEM01", "Chemistry"));
        Course course = TestData.course("BSC-PHY", "B.Sc Physics");
        course.getSubjects().add(maths);
        physics = courses.save(course);
        student = students.save(TestData.student("Kabir", "Mehta", physics));
        flushAndClear();
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    private Course reload(Course course) {
        flushAndClear();
        return courses.findById(course.getId()).orElseThrow();
    }

    // The course form does not carry the curriculum; saving it used to be
    // able to replace the curriculum with the form's empty one
    @Test
    void editingACourseKeepsItsSubjects() throws Exception {
        mvc.perform(post("/course").with(csrf())
                        .param("id", physics.getId().toString())
                        .param("courseCode", "BSC-PHY")
                        .param("courseName", "B.Sc Physics (Hons)")
                        .param("duration", "3 years")
                        .param("fees", "45000"))
                .andExpect(redirectedUrl("/course"));

        Course saved = reload(physics);
        assertThat(saved.getCourseName()).isEqualTo("B.Sc Physics (Hons)");
        assertThat(saved.getSubjects()).extracting(Subject::getSubjectCode).containsExactly("MATH01");
    }

    @Test
    void theCoursePageListsItsSubjectsAndOffersTheOthers() throws Exception {
        mvc.perform(get("/course/view/" + physics.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Mathematics")))
                .andExpect(content().string(containsString("Chemistry (CHEM01)")));
    }

    @Test
    void addingAndRemovingASubject() throws Exception {
        mvc.perform(post("/course/" + physics.getId() + "/subjects").with(csrf())
                        .param("subjectId", chemistry.getId().toString()))
                .andExpect(redirectedUrl("/course/view/" + physics.getId()));
        assertThat(reload(physics).getSubjects()).hasSize(2);

        mvc.perform(post("/course/" + physics.getId() + "/subjects/" + chemistry.getId() + "/remove").with(csrf()))
                .andExpect(redirectedUrl("/course/view/" + physics.getId()));
        assertThat(reload(physics).getSubjects()).extracting(Subject::getSubjectCode).containsExactly("MATH01");
    }

    @Test
    void aSubjectWithEnrollmentsStaysInTheCourse() throws Exception {
        enrollments.save(TestData.enrollment(student, physics, maths));

        mvc.perform(post("/course/" + physics.getId() + "/subjects/" + maths.getId() + "/remove").with(csrf()))
                .andExpect(flash().attribute("error", containsString("1 enrollment uses this subject")));

        assertThat(reload(physics).getSubjects()).hasSize(1);
    }

    private ResultActions enroll(Subject subject) throws Exception {
        return mvc.perform(post("/enrollment").with(csrf())
                .param("student", student.getId().toString())
                .param("course", physics.getId().toString())
                .param("subject", subject.getId().toString())
                .param("enrollmentDate", "2026-08-01"));
    }

    @Test
    void anEnrollmentMustUseASubjectTheCourseTeaches() throws Exception {
        enroll(chemistry)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("This subject is not part of the chosen course.")));
        assertThat(enrollments.count()).isZero();

        enroll(maths).andExpect(redirectedUrl("/enrollment"));
        assertThat(enrollments.count()).isEqualTo(1);
    }

    // G8: the student's course is chosen by id and shown by name
    @Test
    void aStudentShowsTheirCourseName() throws Exception {
        mvc.perform(get("/student/view/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("B.Sc Physics")));
    }

    @Test
    void searchingStudentsByCourseNameFindsThem() throws Exception {
        mvc.perform(get("/student/search").param("keyword", "physics"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Kabir")));
    }

    // The curriculum routes fall under the admin-only POST /course/** rule
    @Test
    @WithMockUser(roles = "TEACHER")
    void onlyAnAdminChangesTheCurriculum() throws Exception {
        mvc.perform(post("/course/" + physics.getId() + "/subjects").with(csrf())
                        .param("subjectId", chemistry.getId().toString()))
                .andExpect(status().isForbidden());
        mvc.perform(post("/course/" + physics.getId() + "/subjects/" + maths.getId() + "/remove").with(csrf()))
                .andExpect(status().isForbidden());

        assertThat(reload(physics).getSubjects()).hasSize(1);
    }

    @Test
    void aCourseWithStudentsExplainsWhyItCannotBeDeleted() throws Exception {
        mvc.perform(post("/course/delete/" + physics.getId()).with(csrf()))
                .andExpect(redirectedUrl("/course"))
                .andExpect(flash().attribute("error", containsString("still has 1 student")));

        assertThat(courses.existsById(physics.getId())).isTrue();
    }
}
