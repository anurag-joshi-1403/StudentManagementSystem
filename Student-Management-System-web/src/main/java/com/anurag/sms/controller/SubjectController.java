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

import com.anurag.sms.entity.Subject;
import com.anurag.sms.service.SubjectService;

import jakarta.validation.Valid;

@Controller
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // Display Subject List
    @GetMapping("/subject")
    public String listSubjects(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        if (keyword != null && !keyword.trim().isEmpty()) {

            model.addAttribute("subjects",
                    subjectService.searchSubjects(keyword));

        } else {

            Page<Subject> subjectPage =
                    subjectService.getSubjectsByPage(page);

            model.addAttribute("subjects",
                    subjectPage.getContent());

            model.addAttribute("currentPage", page);

            model.addAttribute("totalPages",
                    subjectPage.getTotalPages());
        }

        model.addAttribute("keyword", keyword);

        return LayoutView.render(model, "subject/subject-list :: content", "subject", "Subjects");
    }

    // Show Add Subject Form
    @GetMapping("/subject/new")
    public String createSubjectForm(Model model) {

        model.addAttribute("subject", new Subject());

        return LayoutView.render(model, "subject/subject-form :: content", "subject", "Add Subject");
    }

    // Save Subject
    @PostMapping("/subject")
    public String saveSubject(
            @Valid @ModelAttribute("subject") Subject subject,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return LayoutView.render(model, "subject/subject-form :: content", "subject",
                    subject.getId() == null ? "Add Subject" : "Edit Subject");
        }

        // Duplicate Subject Code Check
        if (subject.getId() == null &&
                subjectService.existsBySubjectCode(subject.getSubjectCode())) {

            model.addAttribute(
                    "duplicateError",
                    "Subject Code already exists.");

            return LayoutView.render(model, "subject/subject-form :: content", "subject",
                    subject.getId() == null ? "Add Subject" : "Edit Subject");
        }

        subjectService.saveSubject(subject);

        return "redirect:/subject";
    }

    // Edit Subject
    @GetMapping("/subject/edit/{id}")
    public String editSubject(
            @PathVariable Long id,
            Model model) {

        Subject subject =
                subjectService.getSubjectById(id);

        model.addAttribute("subject", subject);

        return LayoutView.render(model, "subject/subject-form :: content", "subject", "Edit Subject");
    }

    // Delete Subject
    @GetMapping("/subject/delete/{id}")
    public String deleteSubject(
            @PathVariable Long id) {

        subjectService.deleteSubject(id);

        return "redirect:/subject";
    }

}