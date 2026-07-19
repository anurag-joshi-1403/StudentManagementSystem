package com.anurag.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.anurag.sms.entity.Course;
import com.anurag.sms.service.CourseService;

import jakarta.validation.Valid;

@Controller
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course")
    public String listCourses(Model model) {

        model.addAttribute("courses",
                courseService.getAllCourses());

        return "course/course-list";
    }

    @GetMapping("/course/new")
    public String createCourseForm(Model model) {

        model.addAttribute("course",
                new com.anurag.sms.entity.Course());

        return "course/course-form";
    }

    @PostMapping("/course")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
            BindingResult result,
            Model model) {

        // Validation Errors
        if (result.hasErrors()) {
            return "course/course-form";
        }

        // Duplicate Course Code Check
        if (courseService.existsByCourseCode(course.getCourseCode())) {

            model.addAttribute("duplicateError",
                    "Course Code already exists.");

            return "course/course-form";
        }

        // Save Course
        courseService.saveCourse(course);

        return "redirect:/course";
    }

}