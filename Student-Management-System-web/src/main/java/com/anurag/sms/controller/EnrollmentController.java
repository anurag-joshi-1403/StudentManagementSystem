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

        // The list and the search are one paged query: a blank keyword lists
        // every enrollment. The pager links back here with the keyword (#11).
        @GetMapping
        public String listEnrollments(

                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String keyword,
                        Model model) {

                Page<Enrollment> enrollmentPage = enrollmentService.searchEnrollments(keyword, page);

                model.addAttribute("enrollments", enrollmentPage.getContent());

                model.addAttribute("currentPage", enrollmentPage.getNumber() + 1);

                model.addAttribute("totalPages", enrollmentPage.getTotalPages());

                model.addAttribute("totalItems", enrollmentPage.getTotalElements());

                model.addAttribute("keyword", keyword);

                return LayoutView.render(model, "enrollment/enrollment-list :: content", "enrollment", "Enrollments");
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

                return LayoutView.render(model, "enrollment/enrollment-form :: content", "enrollment", "Add Enrollment");
        }

        // ==========================
        // Save Enrollment
        // ==========================

        @PostMapping
        public String saveEnrollment(

                        @Valid @ModelAttribute("enrollment") Enrollment enrollment,

                        BindingResult result,

                        Model model) {

                // A student may be enrolled in a course and subject only once;
                // an edit may keep its own combination (#27).
                if (enrollment.getStudent() != null
                                && enrollment.getCourse() != null
                                && enrollment.getSubject() != null
                                && enrollmentService.isAlreadyEnrolled(
                                                enrollment.getStudent().getId(),
                                                enrollment.getCourse().getId(),
                                                enrollment.getSubject().getId(),
                                                enrollment.getId())) {

                        result.rejectValue("subject", "enrollment.duplicate",
                                        "This student is already enrolled in this course and subject.");
                }

                if (result.hasErrors()) {

                        model.addAttribute("students",
                                        studentService.getAllStudents());

                        model.addAttribute("courses",
                                        courseService.getAllCourses());

                        model.addAttribute("subjects",
                                        subjectService.getAllSubjects());

                        return LayoutView.render(model, "enrollment/enrollment-form :: content", "enrollment",
                                enrollment.getId() == null ? "Add Enrollment" : "Edit Enrollment");
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
                return LayoutView.render(model, "enrollment/enrollment-form :: content", "enrollment", "Edit Enrollment");
        }

        // ==========================
        // View Enrollment
        // ==========================

        @GetMapping("/view/{id}")
        public String viewEnrollment(
                        @PathVariable Long id,
                        Model model) {

                model.addAttribute(
                                "enrollment",
                                enrollmentService.getEnrollmentById(id));

                return LayoutView.render(model, "enrollment/enrollment-view :: content", "enrollment", "Enrollment Details");
        }

        // ==========================
        // Delete Enrollment
        // ==========================

        @PostMapping("/delete/{id}")
        public String deleteEnrollment(
                        @PathVariable Long id) {

                enrollmentService.deleteEnrollment(id);

                return "redirect:/enrollment";
        }

}