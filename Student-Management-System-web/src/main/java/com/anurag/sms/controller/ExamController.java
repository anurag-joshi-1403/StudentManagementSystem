package com.anurag.sms.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Result;
import com.anurag.sms.service.ExamService;
import com.anurag.sms.service.ResultService;
import com.anurag.sms.service.SubjectService;
import com.anurag.sms.utility.GradeCalculator;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;
    private final SubjectService subjectService;
    private final ResultService resultService;

    public ExamController(ExamService examService,
                          SubjectService subjectService,
                          ResultService resultService) {
        this.examService = examService;
        this.subjectService = subjectService;
        this.resultService = resultService;
    }

    // Display Exam List: a search with no filters, page 1. It used to load
    // every exam at once (#11).
    @GetMapping
    public String listExams(Model model) {
        return searchExam(null, null, 1, model);
    }

    // Show Add Exam Form
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("exam", new Exam());
        model.addAttribute("subjects", subjectService.getAllSubjects());

        return LayoutView.render(model, "exam/exam-form :: content", "exam", "Add Exam");
    }

    // Save Exam
    @PostMapping("/save")
    public String saveExam(@Valid @ModelAttribute("exam") Exam exam,
                           BindingResult result,
                           Model model) {

        checkPassingMarks(exam, result);

        // Without @Valid the entity's constraints only fired inside
        // Hibernate at insert time, which surfaced as a 500 page (#12).
        if (result.hasErrors()) {
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return LayoutView.render(model, "exam/exam-form :: content", "exam", "Add Exam");
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

        return LayoutView.render(model, "exam/exam-form :: content", "exam", "Edit Exam");
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

        checkPassingMarks(exam, result);

        if (result.hasErrors()) {
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return LayoutView.render(model, "exam/exam-form :: content", "exam", "Edit Exam");
        }

        examService.updateExam(exam);

        return "redirect:/exam";
    }

    // Passing marks above the total would make every result a fail (#28).
    // A cross-field rule, so it lives here; @Min covers each field alone.
    private static void checkPassingMarks(Exam exam, BindingResult result) {

        if (exam.getPassingMarks() != null && exam.getTotalMarks() != null
                && exam.getPassingMarks() > exam.getTotalMarks()) {

            result.rejectValue("passingMarks", "exam.passingMarks.max",
                    "Passing marks cannot be more than the total marks ("
                            + exam.getTotalMarks() + ").");
        }
    }

    // Exam schedule: upcoming exams grouped by month
    @GetMapping("/schedule")
    public String examSchedule(Model model) {

        model.addAttribute("schedule", examService.getUpcomingSchedule());

        return LayoutView.render(model, "exam/exam-schedule :: content", "exam", "Exam Schedule");
    }

    // View Exam, with its results and how many passed
    @GetMapping("/view/{id}")
    public String viewExam(@PathVariable Long id,
                           Model model) {

        Exam exam = examService.getExamById(id);
        List<Result> results = resultService.getResultsByExam(id);

        long passCount = results.stream()
                .filter(r -> GradeCalculator.PASS.equals(r.getResultStatus()))
                .count();

        model.addAttribute("exam", exam);
        model.addAttribute("results", results);
        model.addAttribute("passCount", passCount);

        return LayoutView.render(model, "exam/exam-view :: content", "exam", "Exam Details");
    }

    // Delete Exam
    @PostMapping("/delete/{id}")
    public String deleteExam(@PathVariable Long id) {

        examService.deleteExam(id);

        return "redirect:/exam";
    }

    // Search Exam. Also serves the plain list (no filters), so the pager
    // links back here with the filters and page 2 stays filtered (#11).
    @GetMapping("/search")
    public String searchExam(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate examDate,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        // A missing keyword stays null: the service treats null and blank
        // as "no keyword filter", so the date can narrow results on its own.
        Page<Exam> results = examService.searchExam(keyword, examDate, page);

        model.addAttribute("examList", results.getContent());
        model.addAttribute("currentPage", results.getNumber() + 1);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());

        // Echoed back so the search boxes and pager keep what was searched for.
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("examDate", examDate);

        return LayoutView.render(model, "exam/exam-list :: content", "exam", "Exams");
    }

    // Kept so existing /exam/page/{n} links and bookmarks still work
    @GetMapping("/page/{pageNo}")
    public String findPaginated(
            @PathVariable int pageNo,
            Model model) {

        return searchExam(null, null, pageNo, model);
    }

}