package com.anurag.sms.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anurag.sms.entity.Attendance;
import com.anurag.sms.service.AttendanceService;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.service.SubjectService;

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

    // Display Attendance List (defaults to paginated view)
    @GetMapping
    public String listAttendance(Model model) {
        return findPaginated(1, model);
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

    // Delete Attendance
    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id) {

        attendanceService.deleteAttendance(id);

        return "redirect:/attendance";
    }

    // Search Attendance
    @GetMapping("/search")
    public String searchAttendance(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate attendanceDate,
            Model model) {

        // A missing keyword stays null: the service treats null and blank
        // as "no keyword filter", so the date can narrow results on its own.
        java.util.List<Attendance> searchResults = attendanceService.searchAttendance(keyword, attendanceDate);

        model.addAttribute("attendanceList", searchResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("attendanceDate", attendanceDate);
        model.addAttribute("totalItems", searchResults.size());
        model.addAttribute("totalAttendance", attendanceService.getTotalAttendance());

        return LayoutView.render(model, "attendance/attendance-list :: content", "attendance", "Attendance");
    }

    // Pagination
    @GetMapping("/page/{pageNo}")
    public String findPaginated(
            @PathVariable int pageNo,
            Model model) {

        Page<Attendance> page = attendanceService.getAttendanceByPage(pageNo);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalAttendance", attendanceService.getTotalAttendance());
        model.addAttribute("attendanceList", page.getContent());

        return LayoutView.render(model, "attendance/attendance-list :: content", "attendance", "Attendance");
    }
}