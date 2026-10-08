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

    // Display Result List: a search with no keyword, page 1. It used to load
    // every result at once (#11).
    @GetMapping
    public String listResults(Model model) {
        return searchResult("", 1, model);
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

        checkMarksWithinTotal(result, bindingResult);

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

        checkMarksWithinTotal(result, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("exams", examService.getAllExams());
            return LayoutView.render(model, "result/result-form :: content", "result", "Edit Result");
        }

        resultService.updateResult(result);

        return "redirect:/result";
    }

    // The only cap used to be @Max(1000), so 120 out of 100 was accepted and
    // graded as 120% (#28). Needs the chosen exam, so it lives here.
    private static void checkMarksWithinTotal(Result result, BindingResult bindingResult) {

        if (result.getObtainedMarks() != null && result.getExam() != null
                && result.getExam().getTotalMarks() != null
                && result.getObtainedMarks() > result.getExam().getTotalMarks()) {

            bindingResult.rejectValue("obtainedMarks", "result.obtainedMarks.max",
                    "Marks cannot be more than this exam's total ("
                            + result.getExam().getTotalMarks() + ").");
        }
    }

    // View Result
    @GetMapping("/view/{id}")
    public String viewResult(@PathVariable Long id,
                             Model model) {

        model.addAttribute("result", resultService.getResultById(id));

        return LayoutView.render(model, "result/result-view :: content", "result", "Result Details");
    }

    // Delete Result
    @PostMapping("/delete/{id}")
    public String deleteResult(@PathVariable Long id) {

        resultService.deleteResult(id);

        return "redirect:/result";
    }

    // Search Result. Also serves the plain list (blank keyword), so the pager
    // links back here with the keyword and page 2 stays filtered (#11).
    @GetMapping("/search")
    public String searchResult(@RequestParam(defaultValue = "") String keyword,
                               @RequestParam(defaultValue = "1") int page,
                               Model model) {

        Page<Result> results = resultService.searchResult(keyword, page);

        model.addAttribute("resultList", results.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", results.getNumber() + 1);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());

        return LayoutView.render(model, "result/result-list :: content", "result", "Results");
    }

    // Kept so existing /result/page/{n} links and bookmarks still work
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable int pageNo,
                                Model model) {

        return searchResult("", pageNo, model);
    }

}