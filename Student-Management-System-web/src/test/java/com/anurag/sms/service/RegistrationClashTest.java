package com.anurag.sms.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.anurag.sms.dto.UserRegistrationDto;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.TeacherRepository;
import com.anurag.sms.repository.UserRepository;
import com.anurag.sms.service.impl.UserServiceImpl;

/**
 * Two registrations with the same username at the same moment (#43). Both
 * pass the "already exists" checks, and the second fails on the unique
 * index. The registration page prints the service's message, which used
 * to be the database's own text, SQL statement included.
 */
class RegistrationClashTest {

    @Test
    void aClashAtSaveTimeGivesAPlainMessage() {
        UserRepository users = mock(UserRepository.class);
        when(users.save(any())).thenThrow(new DataIntegrityViolationException(
                "could not execute statement [Duplicate entry 'neha_rao' for key 'users.username'] "
                        + "[insert into users (created_at,email,enabled,full_name,password,role) values (?,?,?,?,?,?)]"));

        UserService service = new UserServiceImpl(users, mock(StudentRepository.class),
                mock(TeacherRepository.class), new BCryptPasswordEncoder(4),
                mock(ActivityLogService.class), mock(UserSessionService.class));

        UserRegistrationDto form = new UserRegistrationDto();
        form.setFullName("Neha Rao");
        form.setUsername("neha_rao");
        form.setEmail("neha.rao@example.com");
        form.setPassword("abcdefgh");
        form.setConfirmPassword("abcdefgh");

        assertThatThrownBy(() -> service.registerUser(form))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(UserService.ALREADY_REGISTERED);
    }
}
