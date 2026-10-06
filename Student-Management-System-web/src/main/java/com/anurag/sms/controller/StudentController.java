package com.anurag.sms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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

import jakarta.validation.Valid;

@Controller
public class StudentController {
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

        return "student/student-view";
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

        return "student/student-list";
    }

    @GetMapping("/student/new")
    public String createStudentForm(Model model) {
        Student student = new Student();

        model.addAttribute("student", student);
        model.addAttribute("courses", courseService.getAllCourses());
        return "student/student-form";
    }

    @PostMapping("/student")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            @RequestParam("photoFile") MultipartFile photoFile,
            Model model
    ) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            return "student/student-form";
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
            return "student/student-form";
        }

        // Photo Upload Handling
        if (!photoFile.isEmpty()) {
            String uploadDir = "uploads/student-images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();

            Files.copy(
                    photoFile.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            student.setPhoto(fileName);
        } else {
            // Keep existing photo on edit if no new file was uploaded
            if (student.getId() != null) {
                Student existing = studentService.getStudentById(student.getId());
                if (existing != null) {
                    student.setPhoto(existing.getPhoto());
                }
            }
        }

        studentService.saveStudent(student);

        return "redirect:/student";
    }

    @GetMapping("/student/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("courses", courseService.getAllCourses());
        return "student/student-form";
    }

    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/student";
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

        return "student/student-list";
    }

}
