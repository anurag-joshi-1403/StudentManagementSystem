package com.anurag.sms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.anurag.sms.utility.FieldLimits;
import com.anurag.sms.utility.PasswordPolicy;

public class UserRegistrationDto {

    @NotBlank(message = "Full Name is required")
    @Size(max = FieldLimits.TEXT, message = "Full Name" + FieldLimits.TEXT_TOO_LONG)
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(max = FieldLimits.USERNAME, message = FieldLimits.USERNAME_TOO_LONG)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = FieldLimits.TEXT, message = "Email" + FieldLimits.TEXT_TOO_LONG)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = PasswordPolicy.MIN_LENGTH, max = PasswordPolicy.MAX_LENGTH, message = PasswordPolicy.LENGTH_MESSAGE)
    private String password;

    @NotBlank(message = "Confirm Password is required")
    private String confirmPassword;

    // Constructors

    public UserRegistrationDto() {
    }

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}