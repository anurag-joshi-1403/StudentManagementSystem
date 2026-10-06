package com.anurag.sms.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.anurag.sms.entity.Exam;
import com.anurag.sms.service.ExamService;
import com.anurag.sms.service.SubjectService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;
    private final SubjectService subjectService;

    public ExamController(ExamService examService,
                          SubjectService subjectService) {
        this.examService = examService;
        this.subjectService = subjectService;
    }

    // Display Exam List
    @GetMapping
    public String listExams(Model model) {

        model.addAttribute("examList", examService.getAllExams());

        return "exam/exam-list";
    }

    // Show Add Exam Form
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("exam", new Exam());
        model.addAttribute("subjects", subjectService.getAllSubjects());

        return "exam/exam-form";
    }

    // Save Exam
    @PostMapping("/save")
    public String saveExam(@Valid @ModelAttribute("exam") Exam exam,
                           BindingResult result,
                           Model model) {

        // Without @Valid the entity's constraints only fired inside
        // Hibernate at insert time, which surfaced as a 500 page (#12).
        if (result.hasErrors()) {
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "exam/exam-form";
        }

        examService.saveExam(exam);

        return "redirect:/exam";
    }

    // Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model) {

        model.addAttribute("exam",
                examService.getExamById(id));

        model.addAttribute("subjects",
                subjectService.getAllSubjects());

        return "exam/exam-form";
    }

    // Update Exam
    @PostMapping("/update/{id}")
    public String updateExam(@PathVariable Long id,
                             @Valid @ModelAttribute("exam") Exam exam,
                             BindingResult result,
                             Model model) {

        // Set before the error check: the form picks its action from
        // exam.id, so without it a corrected form would post to /save
        // and create a duplicate instead of updating.
        exam.setId(id);

        if (result.hasErrors()) {
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "exam/exam-form";
        }

        examService.updateExam(exam);

        return "redirect:/exam";
    }

    // Delete Exam
    @GetMapping("/delete/{id}")
    public String deleteExam(@PathVariable Long id) {

        examService.deleteExam(id);

        return "redirect:/exam";
    }

    // Search Exam
    @GetMapping("/search")
    public String searchExam(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate examDate,
            Model model) {

        // A missing keyword stays null: the service treats null and blank
        // as "no keyword filter", so the date can narrow results on its own.
        model.addAttribute(
                "examList",
                examService.searchExam(keyword, examDate));

        // Echoed back so the search boxes keep what was searched for.
        model.addAttribute("keyword", keyword);
        model.addAttribute("examDate", examDate);

        return "exam/exam-list";
    }

    // Pagination
    @GetMapping("/page/{pageNo}")
    public String findPaginated(
            @PathVariable int pageNo,
            Model model) {

        Page<Exam> page = examService.getExamByPage(pageNo);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("examList", page.getContent());

        return "exam/exam-list";
    }

}