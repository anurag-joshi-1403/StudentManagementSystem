package com.anurag.sms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.anurag.sms.entity.Result;
import com.anurag.sms.service.ExamService;
import com.anurag.sms.service.ResultService;
import com.anurag.sms.service.StudentService;

import jakarta.validation.Valid;

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

        return LayoutView.render(model, "result/result-list :: content", "result", "Results");
    }

    // Show Add Result Form
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("result", new Result());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("exams", examService.getAllExams());

        return LayoutView.render(model, "result/result-form :: content", "result", "Add Result");
    }

    // Save Result
    @PostMapping("/save")
    public String saveResult(@Valid @ModelAttribute("result") Result result,
                             BindingResult bindingResult,
                             Model model) {

        // Must run before the service: its grade calculation dereferences
        // the exam and the marks, so a blank one used to throw a
        // NullPointerException, and bad marks failed in Hibernate (#12).
        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("exams", examService.getAllExams());
            return LayoutView.render(model, "result/result-form :: content", "result", "Add Result");
        }

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

        return LayoutView.render(model, "result/result-form :: content", "result", "Edit Result");
    }

    // Update Result
    @PostMapping("/update/{id}")
    public String updateResult(@PathVariable Long id,
                               @Valid @ModelAttribute("result") Result result,
                               BindingResult bindingResult,
                               Model model) {

        // Set before the error check: the form picks its action from
        // result.id, so without it a corrected form would post to /save
        // and create a duplicate instead of updating.
        result.setId(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("exams", examService.getAllExams());
            return LayoutView.render(model, "result/result-form :: content", "result", "Edit Result");
        }

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

        return LayoutView.render(model, "result/result-list :: content", "result", "Results");
    }

    // Pagination
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable int pageNo,
                                Model model) {

        Page<Result> page = resultService.getResultByPage(pageNo);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("resultList", page.getContent());

        return LayoutView.render(model, "result/result-list :: content", "result", "Results");
    }

}