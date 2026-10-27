package com.anurag.sms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.User;
import com.anurag.sms.repository.UserRepository;
import com.anurag.sms.service.UserService;

/**
 * #37: disabling an account or changing its role signs it out at once.
 * Before, a session that was already open kept working, with the old role,
 * until it ended.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SessionExpiryTest {

    private static final String PASSWORD = "password-1";

    @Autowired private MockMvc mvc;
    @Autowired private UserRepository users;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    private User admin;
    private User student;

    @BeforeEach
    void accounts() {
        admin = users.save(account("sess_admin", "ROLE_ADMIN"));
        student = users.save(account("sess_student", "ROLE_STUDENT"));
    }

    private User account(String username, String role) {
        User u = new User();
        u.setFullName(username);
        u.setUsername(username);
        u.setEmail(username + "@example.com");
        u.setPassword(passwordEncoder.encode(PASSWORD));
        u.setRole(role);
        u.setEnabled(true);
        return u;
    }

    // Signs in through the real login form and returns the browser session
    private MockHttpSession signIn(String username) throws Exception {
        return (MockHttpSession) mvc.perform(formLogin("/login").user(username).password(PASSWORD))
                .andExpect(redirectedUrl("/dashboard"))
                .andReturn().getRequest().getSession(false);
    }

    @Test
    void disablingAnAccountSignsItOutOnItsNextRequest() throws Exception {
        MockHttpSession session = signIn("sess_student");
        mvc.perform(get("/profile").session(session)).andExpect(status().isOk());

        userService.setEnabled(student.getId(), false, admin.getUsername());

        mvc.perform(get("/profile").session(session))
                .andExpect(redirectedUrl("/login?expired=true"));
    }

    @Test
    void changingARoleSignsTheAccountOutSoTheNewRoleApplies() throws Exception {
        MockHttpSession session = signIn("sess_student");

        userService.changeRole(student.getId(), "ROLE_TEACHER", admin.getUsername());

        mvc.perform(get("/profile").session(session))
                .andExpect(redirectedUrl("/login?expired=true"));
    }

    @Test
    void otherAccountsStaySignedIn() throws Exception {
        MockHttpSession adminSession = signIn("sess_admin");
        signIn("sess_student");

        userService.setEnabled(student.getId(), false, admin.getUsername());

        mvc.perform(get("/profile").session(adminSession)).andExpect(status().isOk());
    }
}
