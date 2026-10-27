package com.anurag.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.anurag.sms.dto.UserRegistrationDto;
import com.anurag.sms.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Show Login Page
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // Show Registration Page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    // Register User. The form is bound as "user", the name the page reads;
    // without the name, any validation error rendered a 500 page (#36).
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") UserRegistrationDto user,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "auth/register";
        }

        // Only the service's own messages are shown. Any other exception
        // goes to the error page, so its text (which can include SQL) never
        // reaches the visitor (#43).
        try {
            userService.registerUser(user);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/register";
        }

        return "redirect:/login?registered";
    }
}