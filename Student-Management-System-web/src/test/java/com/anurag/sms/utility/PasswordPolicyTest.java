package com.anurag.sms.utility;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.anurag.sms.dto.ChangePasswordDto;
import com.anurag.sms.dto.UserRegistrationDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * #36: registration and the change-password form follow one rule, with
 * one message.
 */
class PasswordPolicyTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void start() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void stop() {
        factory.close();
    }

    private static Set<String> registrationMessages(String password) {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFullName("Neha Rao");
        dto.setUsername("neha_rao");
        dto.setEmail("neha.rao@example.com");
        dto.setPassword(password);
        dto.setConfirmPassword(password);
        return messages(validator.validate(dto));
    }

    private static Set<String> changeFormMessages(String password) {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("old-password");
        dto.setNewPassword(password);
        dto.setConfirmPassword(password);
        return messages(validator.validate(dto));
    }

    private static <T> Set<String> messages(Set<ConstraintViolation<T>> violations) {
        return violations.stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    @Test
    void sevenCharactersGiveTheSameMessageOnBothForms() {
        assertThat(registrationMessages("abcdefg")).containsExactly(PasswordPolicy.LENGTH_MESSAGE);
        assertThat(changeFormMessages("abcdefg")).containsExactly(PasswordPolicy.LENGTH_MESSAGE);
    }

    @Test
    void eightAndSeventyTwoCharactersPassOnBothForms() {
        assertThat(registrationMessages("abcdefgh")).isEmpty();
        assertThat(changeFormMessages("abcdefgh")).isEmpty();
        assertThat(registrationMessages("a".repeat(72))).isEmpty();
        assertThat(changeFormMessages("a".repeat(72))).isEmpty();
    }

    // BCrypt rejects more than 72 bytes when the password is saved, so the
    // forms stop it first
    @Test
    void seventyThreeCharactersAreRefusedOnBothForms() {
        assertThat(registrationMessages("a".repeat(73))).containsExactly(PasswordPolicy.LENGTH_MESSAGE);
        assertThat(changeFormMessages("a".repeat(73))).containsExactly(PasswordPolicy.LENGTH_MESSAGE);
    }

    @Test
    void isAcceptableMatchesTheAnnotations() {
        assertThat(PasswordPolicy.isAcceptable(null)).isFalse();
        assertThat(PasswordPolicy.isAcceptable("abcdefg")).isFalse();
        assertThat(PasswordPolicy.isAcceptable("abcdefgh")).isTrue();
        assertThat(PasswordPolicy.isAcceptable("a".repeat(72))).isTrue();
        assertThat(PasswordPolicy.isAcceptable("a".repeat(73))).isFalse();
    }
}
