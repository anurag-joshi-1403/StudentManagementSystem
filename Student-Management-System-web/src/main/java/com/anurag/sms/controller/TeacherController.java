package com.anurag.sms.controller;

import java.io.IOException;

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

import com.anurag.sms.entity.Teacher;
import com.anurag.sms.service.TeacherService;
import com.anurag.sms.utility.FileUploadUtil;

import jakarta.validation.Valid;

@Controller
public class TeacherController {

    private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

    // Served back at /teacher-images/** by WebConfig
    private static final String PHOTO_DIR = "uploads/teacher-images";

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Display Teacher List
    @GetMapping("/teacher")
    public String listTeachers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        // The list and the search are one paged query: a blank keyword lists
        // every teacher. The pager links back here with the keyword (#11).
        Page<Teacher> teacherPage = teacherService.searchTeachers(keyword, page);

        model.addAttribute("teachers", teacherPage.getContent());

        model.addAttribute("currentPage", teacherPage.getNumber() + 1);

        model.addAttribute("totalPages", teacherPage.getTotalPages());

        model.addAttribute("totalItems", teacherPage.getTotalElements());

        model.addAttribute("keyword", keyword);

        return LayoutView.render(model, "teacher/teacher-list :: content", "teacher", "Teachers");
    }

    // Open Add Teacher Form
    @GetMapping("/teacher/new")
    public String createTeacherForm(Model model) {

        model.addAttribute("teacher",
                new Teacher());

        return LayoutView.render(model, "teacher/teacher-form :: content", "teacher", "Add Teacher");
    }

    @PostMapping("/teacher")
    public String saveTeacher(
            @Valid @ModelAttribute("teacher") Teacher teacher,
            BindingResult result,
            @RequestParam("photoFile") MultipartFile photoFile,
            Model model) {

        if (result.hasErrors()) {
            return LayoutView.render(model, "teacher/teacher-form :: content", "teacher",
                    teacher.getId() == null ? "Add Teacher" : "Edit Teacher");
        }

        // Checked on edit as well as create, so an edit cannot take another
        // teacher's email (#27). The database's unique index backs this up.
        if (teacherService.isEmailTaken(teacher.getEmail(), teacher.getId())) {

            model.addAttribute(
                    "duplicateError",
                    "Teacher email already exists.");

            return LayoutView.render(model, "teacher/teacher-form :: content", "teacher",
                    teacher.getId() == null ? "Add Teacher" : "Edit Teacher");
        }

        // Read the current photo name BEFORE saving: the save merges the form
        // into the same managed entity, so afterwards it shows the new name.
        String oldPhoto = teacher.getId() == null
                ? null
                : teacherService.getTeacherById(teacher.getId()).getPhoto();

        if (photoFile.isEmpty()) {
            // No new file chosen: an edit keeps the existing photo
            teacher.setPhoto(oldPhoto);
        } else {
            try {
                teacher.setPhoto(FileUploadUtil.saveImage(photoFile, PHOTO_DIR));
            } catch (IllegalArgumentException e) {
                result.rejectValue("photo", "photo.invalid", e.getMessage());
                return LayoutView.render(model, "teacher/teacher-form :: content", "teacher",
                        teacher.getId() == null ? "Add Teacher" : "Edit Teacher");
            } catch (IOException e) {
                // This failure used to be swallowed (stack trace to the console
                // only), so the teacher was saved without the photo and the
                // user was never told (#29).
                log.error("Could not save photo for teacher {}", teacher.getEmail(), e);
                result.rejectValue("photo", "photo.failed",
                        "The photo could not be saved, so nothing was changed. Please try again.");
                return LayoutView.render(model, "teacher/teacher-form :: content", "teacher",
                        teacher.getId() == null ? "Add Teacher" : "Edit Teacher");
            }
        }

        teacherService.saveTeacher(teacher);

        // Only once the record points at the new photo is the old file removed,
        // so a failed save never leaves the teacher without a photo (#7).
        if (oldPhoto != null && !oldPhoto.equals(teacher.getPhoto())) {
            deletePhoto(oldPhoto);
        }

        return "redirect:/teacher";
    }

    @GetMapping("/teacher/edit/{id}")
    public String editTeacher(@PathVariable Long id, Model model) {

        Teacher teacher = teacherService.getTeacherById(id);

        model.addAttribute("teacher", teacher);

        return LayoutView.render(model, "teacher/teacher-form :: content", "teacher", "Edit Teacher");
    }

    @PostMapping("/teacher/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {

        String photo = teacherService.getTeacherById(id).getPhoto();

        teacherService.deleteTeacher(id);

        deletePhoto(photo);

        return "redirect:/teacher";
    }

    // A leftover file is harmless, so a failed delete is logged, not shown.
    private void deletePhoto(String fileName) {
        try {
            FileUploadUtil.delete(PHOTO_DIR, fileName);
        } catch (IOException e) {
            log.warn("Could not delete old teacher photo {}", fileName, e);
        }
    }

    @GetMapping("/teacher/view/{id}")
    public String viewTeacher(@PathVariable Long id, Model model) {

        Teacher teacher = teacherService.getTeacherById(id);

        model.addAttribute("teacher", teacher);

        return LayoutView.render(model, "teacher/teacher-view :: content", "teacher", "Teacher Profile");
    }

}