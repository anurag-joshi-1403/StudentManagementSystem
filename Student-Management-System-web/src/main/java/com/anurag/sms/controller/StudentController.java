package com.anurag.sms.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.anurag.sms.entity.Student;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.utility.FileUploadUtil;

import jakarta.validation.Valid;

@Controller
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    // Served back at /student-images/** by WebConfig
    private static final String PHOTO_DIR = "uploads/student-images";

    private final StudentService studentService;
    private final com.anurag.sms.service.CourseService courseService;

    public StudentController(StudentService studentService,
                             com.anurag.sms.service.CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/student")
    public String listStudents(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/student/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {

        Student student = studentService.getStudentById(id);

        model.addAttribute("student", student);

        return LayoutView.render(model, "student/student-view :: content", "student", "Student Profile");
    }

    @GetMapping("/student/search")
    public String searchStudents(@RequestParam("keyword") String keyword,
            Model model) {

        List<Student> students = studentService.searchStudents(keyword);

        model.addAttribute("students", students);

        // student-list.html's pager does arithmetic on these, so they must be
        // present even though search results are not paginated yet (#11).
        model.addAttribute("currentPage", 1);

        model.addAttribute("totalPages", 1);

        model.addAttribute("totalItems", students.size());

        return LayoutView.render(model, "student/student-list :: content", "student", "Students");
    }

    @GetMapping("/student/new")
    public String createStudentForm(Model model) {
        Student student = new Student();

        model.addAttribute("student", student);
        model.addAttribute("courses", courseService.getAllCourses());
        return LayoutView.render(model, "student/student-form :: content", "student", "Add Student");
    }

    @PostMapping("/student")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            @RequestParam("photoFile") MultipartFile photoFile,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            return LayoutView.render(model, "student/student-form :: content", "student",
                    student.getId() == null ? "Add Student" : "Edit Student");
        }

        // Duplicate Email Validation (only flag if email belongs to another student)
        boolean emailClash = false;
        if (student.getId() == null) {
            emailClash = studentService.existsByEmail(student.getEmail());
        } else {
            Student existing = studentService.getStudentById(student.getId());
            if (existing != null && !existing.getEmail().equalsIgnoreCase(student.getEmail())) {
                emailClash = studentService.existsByEmail(student.getEmail());
            }
        }

        if (emailClash) {
            result.rejectValue(
                    "email",
                    "error.student",
                    "Email already exists");
            model.addAttribute("courses", courseService.getAllCourses());
            return LayoutView.render(model, "student/student-form :: content", "student",
                    student.getId() == null ? "Add Student" : "Edit Student");
        }

        // Read the current photo name BEFORE saving: the save merges the form
        // into the same managed entity, so afterwards it shows the new name.
        String oldPhoto = student.getId() == null
                ? null
                : studentService.getStudentById(student.getId()).getPhoto();

        if (photoFile.isEmpty()) {
            // No new file chosen: an edit keeps the existing photo
            student.setPhoto(oldPhoto);
        } else {
            try {
                student.setPhoto(FileUploadUtil.saveImage(photoFile, PHOTO_DIR));
            } catch (IllegalArgumentException e) {
                result.rejectValue("photo", "photo.invalid", e.getMessage());
                model.addAttribute("courses", courseService.getAllCourses());
                return LayoutView.render(model, "student/student-form :: content", "student",
                        student.getId() == null ? "Add Student" : "Edit Student");
            } catch (IOException e) {
                log.error("Could not save photo for student {}", student.getEmail(), e);
                result.rejectValue("photo", "photo.failed",
                        "The photo could not be saved, so nothing was changed. Please try again.");
                model.addAttribute("courses", courseService.getAllCourses());
                return LayoutView.render(model, "student/student-form :: content", "student",
                        student.getId() == null ? "Add Student" : "Edit Student");
            }
        }

        studentService.saveStudent(student);

        // Only once the record points at the new photo is the old file removed,
        // so a failed save never leaves the student without a photo (#7).
        if (oldPhoto != null && !oldPhoto.equals(student.getPhoto())) {
            deletePhoto(oldPhoto);
        }

        return "redirect:/student";
    }

    @GetMapping("/student/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("courses", courseService.getAllCourses());
        return LayoutView.render(model, "student/student-form :: content", "student", "Edit Student");
    }

    @PostMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        String photo = studentService.getStudentById(id).getPhoto();
        studentService.deleteStudent(id);
        deletePhoto(photo);
        return "redirect:/student";
    }

    // A leftover file is harmless, so a failed delete is logged, not shown.
    private void deletePhoto(String fileName) {
        try {
            FileUploadUtil.delete(PHOTO_DIR, fileName);
        } catch (IOException e) {
            log.warn("Could not delete old student photo {}", fileName, e);
        }
    }

    @GetMapping("/student/page/{pageNo}")
    public String findPaginated(
            @PathVariable int pageNo,
            Model model) {

        Page<Student> page = studentService.getStudentsByPage(pageNo);

        List<Student> students = page.getContent();

        model.addAttribute("currentPage", pageNo);

        model.addAttribute("totalPages", page.getTotalPages());

        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("students", students);

        return LayoutView.render(model, "student/student-list :: content", "student", "Students");
    }

}
