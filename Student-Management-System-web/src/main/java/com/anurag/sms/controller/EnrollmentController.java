package com.anurag.sms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anurag.sms.entity.Enrollment;
import com.anurag.sms.service.CourseService;
import com.anurag.sms.service.EnrollmentService;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.service.SubjectService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

        private final EnrollmentService enrollmentService;
        private final StudentService studentService;
        private final CourseService courseService;
        private final SubjectService subjectService;

        public EnrollmentController(
                        EnrollmentService enrollmentService,
                        StudentService studentService,
                        CourseService courseService,
                        SubjectService subjectService) {

                this.enrollmentService = enrollmentService;
                this.studentService = studentService;
                this.courseService = courseService;
                this.subjectService = subjectService;
        }

        // ==========================
        // Display Enrollment List
        // ==========================

        @GetMapping
        public String listEnrollments(

                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(required = false) String keyword,
                        Model model) {

                if (keyword != null && !keyword.trim().isEmpty()) {

                        model.addAttribute("enrollments",
                                        enrollmentService.searchEnrollments(keyword));

                } else {

                        Page<Enrollment> enrollmentPage = enrollmentService.getEnrollmentsByPage(page);

                        model.addAttribute("enrollments",
                                        enrollmentPage.getContent());

                        model.addAttribute("currentPage", page);

                        model.addAttribute("totalPages",
                                        enrollmentPage.getTotalPages());
                }

                model.addAttribute("keyword", keyword);

                return "enrollment/enrollment-list";
        }

        // ==========================
        // Show Add Form
        // ==========================

        @GetMapping("/new")
        public String showEnrollmentForm(Model model) {

                model.addAttribute("enrollment", new Enrollment());

                model.addAttribute("students",
                                studentService.getAllStudents());

                model.addAttribute("courses",
                                courseService.getAllCourses());

                model.addAttribute("subjects",
                                subjectService.getAllSubjects());

                return "enrollment/enrollment-form";
        }

        // ==========================
        // Save Enrollment
        // ==========================

        @PostMapping
        public String saveEnrollment(

                        @Valid @ModelAttribute("enrollment") Enrollment enrollment,

                        BindingResult result,

                        Model model) {

                if (result.hasErrors()) {

                        model.addAttribute("students",
                                        studentService.getAllStudents());

                        model.addAttribute("courses",
                                        courseService.getAllCourses());

                        model.addAttribute("subjects",
                                        subjectService.getAllSubjects());

                        return "enrollment/enrollment-form";
                }

                enrollmentService.saveEnrollment(enrollment);

                return "redirect:/enrollment";
        }

        // ==========================
        // Edit Enrollment
        // ==========================

        @GetMapping("/edit/{id}")
        public String editEnrollment(
                        @PathVariable Long id,
                        Model model) {

                model.addAttribute(
                                "enrollment",
                                enrollmentService.getEnrollmentById(id));

                model.addAttribute(
                                "students",
                                studentService.getAllStudents());

                model.addAttribute(
                                "courses",
                                courseService.getAllCourses());
                
                model.addAttribute("subjects",
                        subjectService.getAllSubjects());
                return "enrollment/enrollment-form";
        }

        // ==========================
        // Delete Enrollment
        // ==========================

        @GetMapping("/delete/{id}")
        public String deleteEnrollment(
                        @PathVariable Long id) {

                enrollmentService.deleteEnrollment(id);

                return "redirect:/enrollment";
        }

}