package com.anurag.sms.controller;

import com.anurag.sms.service.StudentService;

import org.springframework.stereotype.Controller;

@Controller
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
}
