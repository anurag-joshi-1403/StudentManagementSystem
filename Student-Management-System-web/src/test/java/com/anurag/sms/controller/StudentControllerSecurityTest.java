package com.anurag.sms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.anurag.sms.TestData;
import com.anurag.sms.config.SecurityConfig;
import com.anurag.sms.service.AttendanceService;
import com.anurag.sms.service.CourseService;
import com.anurag.sms.service.CustomUserDetailsService;
import com.anurag.sms.service.NotificationService;
import com.anurag.sms.service.PdfService;
import com.anurag.sms.service.ResultService;
import com.anurag.sms.service.StudentService;

/**
 * F17: the role rules from C7/C9, checked against the real SecurityConfig.
 * Only the web layer is started; the services are mocks.
 */
@WebMvcTest(StudentController.class)
@Import(SecurityConfig.class)
class StudentControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean private StudentService studentService;
    @MockitoBean private CourseService courseService;
    @MockitoBean private ResultService resultService;
    @MockitoBean private AttendanceService attendanceService;
    @MockitoBean private PdfService pdfService;
    @MockitoBean private NotificationService notificationService;
    @MockitoBean private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "STUDENT")
    void aStudentCannotDelete() throws Exception {
        mvc.perform(post("/student/delete/1").with(csrf()))
                .andExpect(status().isForbidden());

        verify(studentService, never()).deleteStudent(1L);
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void aTeacherCannotDeleteAStudentEither() throws Exception {
        mvc.perform(post("/student/delete/1").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void anAdminDeletesAndIsRedirectedToTheList() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(TestData.student("Kabir", "Mehta"));

        mvc.perform(post("/student/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student"));

        verify(studentService).deleteStudent(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void aDeleteWithoutTheCsrfTokenIsRefused() throws Exception {
        mvc.perform(post("/student/delete/1"))
                .andExpect(status().isForbidden());

        verify(studentService, never()).deleteStudent(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void aDeleteIsNeverAGet() throws Exception {
        mvc.perform(get("/student/delete/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void signedOutUsersAreSentToTheLoginPage() throws Exception {
        mvc.perform(post("/student/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void onlyAdminsCanExportStudents() throws Exception {
        mvc.perform(get("/student/export"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void theExportIsACsvAttachment() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of());

        mvc.perform(get("/student/export"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv;charset=UTF-8"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"students.csv\""));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void everyoneCanDownloadAMarksheetPdf() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(TestData.student("Kabir", "Mehta"));
        when(pdfService.marksheet(any(), any())).thenReturn("%PDF-".getBytes());

        mvc.perform(get("/student/1/marksheet.pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"marksheet-kabir-mehta.pdf\""));
    }
}
