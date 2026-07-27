package com.anurag.sms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
    public String listTeachers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        if (keyword != null && !keyword.trim().isEmpty()) {

            model.addAttribute(
                    "teachers",
                    teacherService.searchTeachers(keyword));

        } else {
            Page<Teacher> teacherPage = teacherService.getTeachersByPage(page);

            model.addAttribute("teachers", teacherPage.getContent());

            model.addAttribute("currentPage", page);

            model.addAttribute("totalPages", teacherPage.getTotalPages());
        }

        model.addAttribute("keyword", keyword);

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
            @RequestParam("photoFile") MultipartFile photoFile,
            Model model) {

        if (result.hasErrors()) {
            return "teacher/teacher-form";
        }

        if (teacher.getId() == null &&
                teacherService.existsByEmail(teacher.getEmail())) {

            model.addAttribute(
                    "duplicateError",
                    "Teacher email already exists.");

            return "teacher/teacher-form";
        }

        if (!photoFile.isEmpty()) {

            try {

                String uploadDir = "uploads/teacher-images/";

                Files.createDirectories(Paths.get(uploadDir));

                String fileName = System.currentTimeMillis()
                        + "_"
                        + photoFile.getOriginalFilename();

                Path filePath = Paths.get(uploadDir, fileName);

                Files.copy(
                        photoFile.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                teacher.setPhoto(fileName);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
        else {
            if(teacher.getId() != null){
                Teacher existingTeacher = teacherService.getTeacherById(teacher.getId());
                teacher.setPhoto(existingTeacher.getPhoto());
            }
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

    @GetMapping("/teacher/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {

        teacherService.deleteTeacher(id);

        return "redirect:/teacher";
    }

    @GetMapping("/teacher/view/{id}")
    public String viewTeacher(@PathVariable Long id, Model model) {

        Teacher teacher = teacherService.getTeacherById(id);

        System.out.println("Teacher Photo = " + teacher.getPhoto());

        model.addAttribute("teacher", teacher);

        return "teacher/teacher-view";
    }

}