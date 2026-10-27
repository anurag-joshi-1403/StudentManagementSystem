package com.anurag.sms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.TestData;
import com.anurag.sms.dto.UserRegistrationDto;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.User;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.UserRepository;

/**
 * G10: an admin links an account to the student or teacher it belongs to.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountLinkTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository users;
    @Autowired private StudentRepository students;
    @Autowired private CourseRepository courses;
    @Autowired private MockMvc mvc;

    private Student aarav;
    private User aaravAccount;
    private User otherStudentAccount;
    private User teacherAccount;

    @BeforeEach
    void data() {
        Course course = courses.save(TestData.course("BSC-PHY", "B.Sc Physics"));
        aarav = students.save(TestData.student("Aarav", "Sharma", course));
        aaravAccount = users.save(account("aarav01", "ROLE_STUDENT"));
        otherStudentAccount = users.save(account("diya01", "ROLE_STUDENT"));
        teacherAccount = users.save(account("meera01", "ROLE_TEACHER"));
    }

    private static User account(String username, String role) {
        User u = new User();
        u.setFullName(username);
        u.setUsername(username);
        u.setEmail(username + "@example.com");
        u.setPassword("not-a-real-hash");
        u.setRole(role);
        return u;
    }

    @Test
    void aStudentAccountLinksToAStudent() {
        User linked = userService.linkStudent(aaravAccount.getId(), aarav.getId());

        assertThat(linked.getStudent().getId()).isEqualTo(aarav.getId());
        assertThat(users.findStudentIdByUsername("aarav01")).contains(aarav.getId());
    }

    @Test
    void aStudentBelongsToOneAccountAtMost() {
        userService.linkStudent(aaravAccount.getId(), aarav.getId());

        assertThatThrownBy(() -> userService.linkStudent(otherStudentAccount.getId(), aarav.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Aarav Sharma is already linked to the account aarav01.");
    }

    @Test
    void onlyAStudentAccountLinksToAStudent() {
        assertThatThrownBy(() -> userService.linkStudent(teacherAccount.getId(), aarav.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only a STUDENT account can be linked to a student.");
    }

    @Test
    void aNewRoleDropsALinkThatNoLongerFits() {
        userService.linkStudent(aaravAccount.getId(), aarav.getId());

        userService.changeRole(aaravAccount.getId(), "ROLE_TEACHER", "someone_else");

        assertThat(users.findById(aaravAccount.getId()).orElseThrow().getStudent()).isNull();
        assertThat(users.findStudentIdByUsername("aarav01")).isEmpty();
    }

    @Test
    void unlinkingClearsTheLink() {
        userService.linkStudent(aaravAccount.getId(), aarav.getId());

        userService.unlink(aaravAccount.getId());

        assertThat(users.findStudentIdByUsername("aarav01")).isEmpty();
    }

    // Until emails are verified (L3), matching on email would let anyone who
    // knows a student's address claim their records, so registering never links
    @Test
    void registeringWithAStudentsEmailDoesNotLink() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFullName("Someone");
        dto.setUsername("not_aarav");
        dto.setEmail(aarav.getEmail());
        dto.setPassword("password-1");
        dto.setConfirmPassword("password-1");

        userService.registerUser(dto);

        assertThat(users.findStudentIdByUsername("not_aarav")).isEmpty();
    }

    // The link routes fall under the admin-only /admin/** rule
    @Test
    @WithMockUser(username = "aarav01", roles = "STUDENT")
    void onlyAnAdminLinksAccounts() throws Exception {
        mvc.perform(post("/admin/users/" + aaravAccount.getId() + "/link").with(csrf())
                        .param("studentId", aarav.getId().toString()))
                .andExpect(status().isForbidden());
        mvc.perform(post("/admin/users/" + aaravAccount.getId() + "/unlink").with(csrf()))
                .andExpect(status().isForbidden());

        assertThat(users.findStudentIdByUsername("aarav01")).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin_g10", roles = "ADMIN")
    void theUsersPageShowsTheLink() throws Exception {
        userService.linkStudent(aaravAccount.getId(), aarav.getId());

        mvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Aarav Sharma")))
                .andExpect(content().string(containsString("Unlink")));
    }
}
