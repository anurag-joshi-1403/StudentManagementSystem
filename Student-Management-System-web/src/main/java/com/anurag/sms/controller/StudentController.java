package com.anurag.sms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.anurag.sms.entity.Student;
import com.anurag.sms.service.StudentService;


@Controller
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student")
    public String listStudents(Model model) {
        List<Student> student = studentService.getAllStudents();

        model.addAttribute("students", student);

        return "student/student-list";
    }
}
