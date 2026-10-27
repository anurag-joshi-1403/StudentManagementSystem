package com.anurag.sms.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.service.CourseService;
import com.anurag.sms.service.SubjectService;

import jakarta.validation.Valid;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final SubjectService subjectService;

    public CourseController(CourseService courseService, SubjectService subjectService) {
        this.courseService = courseService;
        this.subjectService = subjectService;
    }

    // The list and the search are one paged query: a blank keyword lists
    // every course. The pager links back here with the keyword (#11).
    @GetMapping("/course")
    public String listCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        Page<Course> coursePage = courseService.searchCourses(keyword, page);

        model.addAttribute("courses", coursePage.getContent());

        model.addAttribute("currentPage", coursePage.getNumber() + 1);

        model.addAttribute("totalPages", coursePage.getTotalPages());

        model.addAttribute("totalItems", coursePage.getTotalElements());

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

        // Checked on edit as well as create, so an edit cannot take another
        // course's code (#27). The database's unique index backs this up.
        if (courseService.isCourseCodeTaken(course.getCourseCode(), course.getId())) {

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

    // The course, with its curriculum (G9) and the subjects that could be added
    @GetMapping("/course/view/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);

        Set<Long> taught = course.getSubjects().stream().map(Subject::getId).collect(Collectors.toSet());
        List<Subject> addable = subjectService.getAllSubjects().stream()
                .filter(subject -> !taught.contains(subject.getId()))
                .toList();

        model.addAttribute("course", course);
        model.addAttribute("addableSubjects", addable);
        return LayoutView.render(model, "course/course-view :: content", "course", "Course Details");
    }

    // Curriculum changes (G9). POST /course/** is admin-only in SecurityConfig.
    @PostMapping("/course/{id}/subjects")
    public String addSubject(@PathVariable Long id, @RequestParam Long subjectId) {

        courseService.addSubject(id, subjectId);

        return "redirect:/course/view/" + id;
    }

    @PostMapping("/course/{id}/subjects/{subjectId}/remove")
    public String removeSubject(@PathVariable Long id,
                                @PathVariable Long subjectId,
                                RedirectAttributes redirect) {
        try {
            courseService.removeSubject(id, subjectId);
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/course/view/" + id;
    }

    @PostMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirect) {

        // Refused while students still belong to the course (G8)
        try {
            courseService.deleteCourse(id);
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/course";
    }
}