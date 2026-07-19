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

        long total = studentService.getTotalStudents();
        long male = studentService.getMaleStudents();
        long female = studentService.getFemaleStudents();

        System.out.println("Total Students : " + total);
        System.out.println("Male Students  : " + male);
        System.out.println("Female Students: " + female);

        model.addAttribute("totalStudents", total);
        model.addAttribute("maleStudents", male);
        model.addAttribute("femaleStudents", female);

        return "dashboard";
    }
}