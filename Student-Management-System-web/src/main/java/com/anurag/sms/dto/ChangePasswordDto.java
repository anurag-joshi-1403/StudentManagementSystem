package com.anurag.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * The change-password form on the profile page (F10). The controller checks
 * that the two new entries match; the service checks the current password.
 */
public class ChangePasswordDto {

    @NotBlank(message = "Enter your current password")
    private String currentPassword;

    @NotBlank(message = "Enter a new password")
    @Size(min = 8, max = 100, message = "The new password must be at least 8 characters")
    private String newPassword;

    @NotBlank(message = "Type the new password again")
    private String confirmPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
