package com.anurag.sms.service;

import java.util.List;

import com.anurag.sms.dto.UserRegistrationDto;
import com.anurag.sms.entity.User;

public interface UserService {

    User registerUser(UserRegistrationDto registrationDto);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // The roles an admin can give an account (F12)
    List<String> ROLES = List.of("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT");

    // Profile page (F9): the signed-in user's own account
    User getByUsername(String username);

    // Change password (F10). Throws IllegalArgumentException when the
    // current password is wrong, or the new one equals it.
    String WRONG_CURRENT_PASSWORD = "Your current password is not correct.";

    void changePassword(String username, String currentPassword, String newPassword);

    // Admin user list (F11), oldest account first
    List<User> getAllUsers();

    // Enable or disable an account (F12). A disabled account cannot sign
    // in. Admins cannot disable themselves.
    User setEnabled(Long userId, boolean enabled, String actingUsername);

    // Change an account's role (F12). Admins cannot change their own role,
    // so there is always at least one admin left.
    User changeRole(Long userId, String role, String actingUsername);
}