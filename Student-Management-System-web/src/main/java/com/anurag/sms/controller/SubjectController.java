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
import com.anurag.sms.service.ExamService;
import com.anurag.sms.service.SubjectService;

import jakarta.validation.Valid;

@Controller
public class SubjectController {

    private final SubjectService subjectService;
    private final ExamService examService;

    public SubjectController(SubjectService subjectService,
                             ExamService examService) {
        this.subjectService = subjectService;
        this.examService = examService;
    }

    // Display Subject List
    // The list and the search are one paged query: a blank keyword lists
    // every subject. The pager links back here with the keyword (#11).
    @GetMapping("/subject")
    public String listSubjects(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        Page<Subject> subjectPage = subjectService.searchSubjects(keyword, page);

        model.addAttribute("subjects", subjectPage.getContent());

        model.addAttribute("currentPage", subjectPage.getNumber() + 1);

        model.addAttribute("totalPages", subjectPage.getTotalPages());

        model.addAttribute("totalItems", subjectPage.getTotalElements());

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

        // Duplicate Subject Code Check, on edit as well as create (#27).
        // The database's unique index backs this up.
        if (subjectService.isSubjectCodeTaken(subject.getSubjectCode(), subject.getId())) {

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

    // View Subject, with its exams
    @GetMapping("/subject/view/{id}")
    public String viewSubject(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("subject", subjectService.getSubjectById(id));
        model.addAttribute("exams", examService.getExamsBySubject(id));

        return LayoutView.render(model, "subject/subject-view :: content", "subject", "Subject Details");
    }

    // Delete Subject
    @PostMapping("/subject/delete/{id}")
    public String deleteSubject(
            @PathVariable Long id) {

        subjectService.deleteSubject(id);

        return "redirect:/subject";
    }

}