package com.anurag.sms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/welcome")
    public String home(){
        return "Welcome to Student Management System.";
    }
}
