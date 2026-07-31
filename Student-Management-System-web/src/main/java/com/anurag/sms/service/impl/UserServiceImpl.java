package com.anurag.sms.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anurag.sms.dto.UserRegistrationDto;
import com.anurag.sms.entity.User;
import com.anurag.sms.repository.UserRepository;
import com.anurag.sms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDto registrationDto) {

        // Check duplicate username
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }

        // Check duplicate email
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        // Check password confirmation
        if (!registrationDto.getPassword()
                .equals(registrationDto.getConfirmPassword())) {

            throw new RuntimeException("Passwords do not match.");
        }

        User user = new User();

        user.setFullName(registrationDto.getFullName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());

        // Encrypt password
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        // Default Role
        user.setRole("ROLE_STUDENT");

        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {

        Optional<User> user = userRepository.findByUsername(username);

        return user.orElse(null);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}