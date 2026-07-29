package com.anurag.sms.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.anurag.sms.entity.Exam;
import com.anurag.sms.service.ExamService;
import com.anurag.sms.service.SubjectService;

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
    public String saveExam(@ModelAttribute("exam") Exam exam) {

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
                             @ModelAttribute Exam exam) {

        exam.setId(id);

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

        if (keyword == null) {
            keyword = "";
        }

        model.addAttribute(
                "examList",
                examService.searchExam(keyword, examDate));

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