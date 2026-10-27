package com.anurag.sms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.repository.UserRepository;
import com.anurag.sms.utility.PasswordPolicy;

/**
 * The registration form (#36). It used to accept 6-character passwords, and
 * any validation error gave a 500 page instead of a message, because the
 * form was bound under a different name from the one the page reads.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegistrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository users;

    private static MockHttpServletRequestBuilder register(String password) {
        return register("Neha Rao", password);
    }

    private static MockHttpServletRequestBuilder register(String fullName, String password) {
        return post("/register").with(csrf())
                .param("fullName", fullName)
                .param("username", "neha_rao")
                .param("email", "neha.rao@example.com")
                .param("password", password)
                .param("confirmPassword", password);
    }

    @Test
    void aSevenCharacterPasswordIsRefusedWithThePolicyMessage() throws Exception {
        mvc.perform(register("abcdefg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(PasswordPolicy.LENGTH_MESSAGE)));

        assertThat(users.existsByUsername("neha_rao")).isFalse();
    }

    @Test
    void aBlankFieldShowsItsMessageOnTheForm() throws Exception {
        mvc.perform(register("", "abcdefgh"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Full Name is required")));

        assertThat(users.existsByUsername("neha_rao")).isFalse();
    }

    @Test
    void anEightCharacterPasswordRegisters() throws Exception {
        mvc.perform(register("abcdefgh"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        assertThat(users.existsByUsername("neha_rao")).isTrue();
    }
}
