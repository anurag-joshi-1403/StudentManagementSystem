package com.anurag.sms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String listCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        if (keyword != null && !keyword.trim().isEmpty()) {

            model.addAttribute("courses",
                    courseService.searchCourses(keyword));

        } else {
            Page<Course> coursePage = courseService.getCoursesByPage(page);

            model.addAttribute("courses", coursePage.getContent());

            model.addAttribute("currentPage", page);

            model.addAttribute("totalPages", coursePage.getTotalPages());

        }

        model.addAttribute("keyword", keyword);

        return LayoutView.render(model, "course/course-list :: content", "course", "Courses");
    }

    @GetMapping("/course/new")
    public String createCourseForm(Model model) {

        model.addAttribute("course",
                new com.anurag.sms.entity.Course());

        return LayoutView.render(model, "course/course-form :: content", "course", "Add Course");
    }

    @PostMapping("/course")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
            BindingResult result,
            Model model) {

        // Validation Errors
        if (result.hasErrors()) {
            return LayoutView.render(model, "course/course-form :: content", "course",
                    course.getId() == null ? "Add Course" : "Edit Course");
        }

        // Duplicate check only for NEW course
        if (course.getId() == null &&
                courseService.existsByCourseCode(course.getCourseCode())) {

            model.addAttribute("duplicateError",
                    "Course Code already exists.");

            return LayoutView.render(model, "course/course-form :: content", "course",
                    course.getId() == null ? "Add Course" : "Edit Course");
        }

        courseService.saveCourse(course);

        return "redirect:/course";
    }

    @GetMapping("/course/edit/{id}")
    public String editCourse(@PathVariable Long id, Model model) {

        Course course = courseService.getCourseById(id);

        model.addAttribute("course", course);

        return LayoutView.render(model, "course/course-form :: content", "course", "Edit Course");
    }

    @GetMapping("/course/view/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return LayoutView.render(model, "course/course-view :: content", "course", "Course Details");
    }

    @PostMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {

        courseService.deleteCourse(id);

        return "redirect:/course";
    }
}