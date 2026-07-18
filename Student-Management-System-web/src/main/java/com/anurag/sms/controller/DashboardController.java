package com.anurag.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.anurag.sms.service.StudentService;

@Controller
public class DashboardController {

    private final StudentService studentService;

    public DashboardController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        model.addAttribute("totalStudents", studentService.getTotalStudents());
        model.addAttribute("maleStudents", studentService.getMaleStudents());
        model.addAttribute("femaleStudents", studentService.getFemaleStudents());

        return "dashboard";
    }
}