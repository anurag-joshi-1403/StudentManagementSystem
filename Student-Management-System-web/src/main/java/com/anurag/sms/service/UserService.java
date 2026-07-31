package com.anurag.sms.service;

import com.anurag.sms.dto.UserRegistrationDto;
import com.anurag.sms.entity.User;

public interface UserService {

    User registerUser(UserRegistrationDto registrationDto);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}