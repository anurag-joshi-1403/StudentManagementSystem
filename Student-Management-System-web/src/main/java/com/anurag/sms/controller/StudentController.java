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

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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

        return "student/student-list";
    }

    @GetMapping("/student/new")
    public String createStudentForm(Model model) {
        Student student = new Student();

        model.addAttribute("student", student);
        return "student/student-form";
    }

    @PostMapping("/student")
    public String saveStudent(

            @Valid @ModelAttribute("student") Student student,
            BindingResult result,

            @RequestParam("photoFile") MultipartFile photoFile

    ) throws IOException {

        if (result.hasErrors()) {
            return "student/student-form";
        }

        // Duplicate Email Validation
        if (studentService.existsByEmail(student.getEmail())) {

            result.rejectValue(
                    "email",
                    "error.student",
                    "Email already exists");

            return "student/student-form";
        }

        // Photo Upload
        if (!photoFile.isEmpty()) {

            String fileName = photoFile.getOriginalFilename();

            Path uploadPath = Paths.get("uploads/student-images");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(
                    photoFile.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            student.setPhoto(fileName);
        }

        studentService.saveStudent(student);

        return "redirect:/student";
    }

    @GetMapping("/student/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
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
