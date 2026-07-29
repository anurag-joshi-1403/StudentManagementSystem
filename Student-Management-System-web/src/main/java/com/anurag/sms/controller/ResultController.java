package com.anurag.sms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.anurag.sms.entity.Result;
import com.anurag.sms.service.ExamService;
import com.anurag.sms.service.ResultService;
import com.anurag.sms.service.StudentService;

@Controller
@RequestMapping("/result")
public class ResultController {

    private final ResultService resultService;
    private final StudentService studentService;
    private final ExamService examService;

    public ResultController(ResultService resultService,
                            StudentService studentService,
                            ExamService examService) {
        this.resultService = resultService;
        this.studentService = studentService;
        this.examService = examService;
    }

    // Display All Results
    @GetMapping
    public String listResults(Model model) {

        model.addAttribute("resultList", resultService.getAllResults());

        return "result/result-list";
    }

    // Show Add Result Form
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("result", new Result());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("exams", examService.getAllExams());

        return "result/result-form";
    }

    // Save Result
    @PostMapping("/save")
    public String saveResult(@ModelAttribute("result") Result result) {

        resultService.saveResult(result);

        return "redirect:/result";
    }

    // Show Edit Result Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model) {

        model.addAttribute("result",
                resultService.getResultById(id));

        model.addAttribute("students",
                studentService.getAllStudents());

        model.addAttribute("exams",
                examService.getAllExams());

        return "result/result-form";
    }

    // Update Result
    @PostMapping("/update/{id}")
    public String updateResult(@PathVariable Long id,
                               @ModelAttribute Result result) {

        result.setId(id);

        resultService.updateResult(result);

        return "redirect:/result";
    }

    // Delete Result
    @GetMapping("/delete/{id}")
    public String deleteResult(@PathVariable Long id) {

        resultService.deleteResult(id);

        return "redirect:/result";
    }

    // Search Result
    @GetMapping("/search")
    public String searchResult(@RequestParam(required = false) String keyword,
                               Model model) {

        if (keyword == null) {
            keyword = "";
        }

        model.addAttribute(
                "resultList",
                resultService.searchResult(keyword));

        return "result/result-list";
    }

    // Pagination
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable int pageNo,
                                Model model) {

        Page<Result> page = resultService.getResultByPage(pageNo);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("resultList", page.getContent());

        return "result/result-list";
    }

}