package com.anurag.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.anurag.sms.entity.Teacher;
import com.anurag.sms.service.TeacherService;

import jakarta.validation.Valid;

@Controller
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Display Teacher List
    @GetMapping("/teacher")
    public String listTeachers(Model model) {

        model.addAttribute("teachers",
                teacherService.getAllTeachers());

        return "teacher/teacher-list";
    }

    // Open Add Teacher Form
    @GetMapping("/teacher/new")
    public String createTeacherForm(Model model) {

        model.addAttribute("teacher",
                new Teacher());

        return "teacher/teacher-form";
    }

    @PostMapping("/teacher")
    public String saveTeacher(
            @Valid @ModelAttribute("teacher") Teacher teacher,
            BindingResult result,
            Model model) {

        // Validation Errors
        if (result.hasErrors()) {
            return "teacher/teacher-form";
        }

        // Duplicate Email Check (only for new teacher)
        if (teacher.getId() == null &&
                teacherService.existsByEmail(teacher.getEmail())) {

            model.addAttribute(
                    "duplicateError",
                    "Teacher Email already exists.");

            return "teacher/teacher-form";
        }

        teacherService.saveTeacher(teacher);

        return "redirect:/teacher";
    }

    @GetMapping("/teacher/edit/{id}")
    public String editTeacher(@PathVariable Long id, Model model) {

        Teacher teacher = teacherService.getTeacherById(id);

        model.addAttribute("teacher", teacher);

        return "teacher/teacher-form";
    }

}