package com.anurag.sms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/student/search")
    public String searchStudents(@RequestParam("keyword") String keyword,
            Model model) {

        List<Student> students = studentService.searchStudents(keyword);

        model.addAttribute("students", students);

        return "student/student-list";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        Student student = new Student();

        model.addAttribute("student", student);
        return "student/student-form";
    }

    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        studentService.saveStudent(student);

        redirectAttributes.addFlashAttribute("successMessage", "Student Added Successfully!");

        return "redirect:/students/new";
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

}
