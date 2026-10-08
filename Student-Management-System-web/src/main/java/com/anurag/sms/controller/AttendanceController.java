package com.anurag.sms.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anurag.sms.dto.AttendanceSummary;
import com.anurag.sms.dto.BulkAttendanceForm;
import com.anurag.sms.entity.Attendance;
import com.anurag.sms.service.AttendanceService;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.service.SubjectService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    public AttendanceController(AttendanceService attendanceService,
                                StudentService studentService,
                                SubjectService subjectService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    // Display Attendance List: a search with no filters, page 1
    @GetMapping
    public String listAttendance(Model model) {
        return searchAttendance(null, null, 1, model);
    }

    // Show Add Attendance Form
    @GetMapping("/new")
    public String createAttendanceForm(Model model) {

        model.addAttribute("attendance", new Attendance());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subjects", subjectService.getAllSubjects());

        return LayoutView.render(model, "attendance/attendance-form :: content", "attendance", "Add Attendance");
    }

    // Save Attendance with validation
    @PostMapping("/save")
    public String saveAttendance(
            @jakarta.validation.Valid @ModelAttribute("attendance") Attendance attendance,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return LayoutView.render(model, "attendance/attendance-form :: content", "attendance", "Add Attendance");
        }

        attendanceService.saveAttendance(attendance);

        return "redirect:/attendance";
    }

    // Show Edit Form
    @GetMapping("/edit/{id}")
    public String editAttendance(@PathVariable Long id, Model model) {

        model.addAttribute("attendance",
                attendanceService.getAttendanceById(id));

        model.addAttribute("students",
                studentService.getAllStudents());

        model.addAttribute("subjects",
                subjectService.getAllSubjects());

        return LayoutView.render(model, "attendance/attendance-form :: content", "attendance", "Edit Attendance");
    }

    // Update Attendance with validation
    @PostMapping("/update/{id}")
    public String updateAttendance(
            @PathVariable Long id,
            @jakarta.validation.Valid @ModelAttribute("attendance") Attendance attendance,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return LayoutView.render(model, "attendance/attendance-form :: content", "attendance", "Edit Attendance");
        }

        attendance.setId(id);

        attendanceService.updateAttendance(attendance);

        return "redirect:/attendance";
    }

    // Spring stops growing a bound list at 256 rows by default, which would
    // break the bulk form for a class bigger than that
    @InitBinder("bulk")
    public void allowLargeClasses(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(2000);
    }

    // Bulk attendance, step 1: pick a subject and date. Step 2 (both chosen):
    // one row per student, each with Present, Absent and Late.
    @GetMapping("/bulk")
    public String bulkForm(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) LocalDate date,
            Model model) {

        BulkAttendanceForm form = new BulkAttendanceForm();
        form.setSubjectId(subjectId);
        form.setDate(date != null ? date : LocalDate.now());

        if (subjectId != null) {
            model.addAttribute("subject", subjectService.getSubjectById(subjectId));
            form.setEntries(attendanceService.getBulkEntries(subjectId, form.getDate()));
        }

        model.addAttribute("bulk", form);
        model.addAttribute("subjects", subjectService.getAllSubjects());

        return LayoutView.render(model, "attendance/attendance-bulk :: content", "attendance", "Bulk Attendance");
    }

    // Bulk attendance, save: one record per student, skipping anyone already
    // recorded for this subject and date
    @PostMapping("/bulk")
    public String saveBulk(
            @Valid @ModelAttribute("bulk") BulkAttendanceForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {
            if (form.getSubjectId() != null) {
                model.addAttribute("subject", subjectService.getSubjectById(form.getSubjectId()));
            }
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return LayoutView.render(model, "attendance/attendance-bulk :: content", "attendance", "Bulk Attendance");
        }

        int saved = attendanceService.saveBulk(form);
        int skipped = form.getEntries().size() - saved;

        redirect.addFlashAttribute("message", "Saved " + saved + " · skipped " + skipped);

        // Back to the list, filtered to the day just recorded
        return "redirect:/attendance/search?attendanceDate=" + form.getDate();
    }

    // Attendance report: one row per student, present out of total,
    // optionally for one subject. Under 75% is highlighted.
    @GetMapping("/report")
    public String attendanceReport(
            @RequestParam(required = false) Long subjectId,
            Model model) {

        model.addAttribute("rows", attendanceService.getAttendanceReport(subjectId));
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("minimumPercent", AttendanceSummary.MINIMUM_PERCENT);

        return LayoutView.render(model, "attendance/attendance-report :: content", "attendance", "Attendance Report");
    }

    // View Attendance
    @GetMapping("/view/{id}")
    public String viewAttendance(@PathVariable Long id, Model model) {

        model.addAttribute("attendance",
                attendanceService.getAttendanceById(id));

        return LayoutView.render(model, "attendance/attendance-view :: content", "attendance", "Attendance Details");
    }

    // Delete Attendance
    @PostMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id) {

        attendanceService.deleteAttendance(id);

        return "redirect:/attendance";
    }

    // Search Attendance. Also serves the plain list (no filters), so the
    // pager links back here with the filters and page 2 stays filtered (#11).
    @GetMapping("/search")
    public String searchAttendance(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate attendanceDate,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        // A missing keyword stays null: the service treats null and blank
        // as "no keyword filter", so the date can narrow results on its own.
        Page<Attendance> results = attendanceService.searchAttendance(keyword, attendanceDate, page);

        model.addAttribute("attendanceList", results.getContent());
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("attendanceDate", attendanceDate);
        model.addAttribute("currentPage", results.getNumber() + 1);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        model.addAttribute("totalAttendance", attendanceService.getTotalAttendance());

        return LayoutView.render(model, "attendance/attendance-list :: content", "attendance", "Attendance");
    }

    // Kept so existing /attendance/page/{n} links and bookmarks still work
    @GetMapping("/page/{pageNo}")
    public String findPaginated(
            @PathVariable int pageNo,
            Model model) {

        return searchAttendance(null, null, pageNo, model);
    }
}