package com.anurag.sms.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    // Display Attendance List
    @GetMapping
    public String listAttendance(Model model) {

        model.addAttribute("attendanceList", attendanceService.getAllAttendance());

        return "attendance/attendance-list";
    }

    // Show Add Attendance Form
    @GetMapping("/new")
    public String createAttendanceForm(Model model) {

        model.addAttribute("attendance", new Attendance());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subjects", subjectService.getAllSubjects());

        return "attendance/attendance-form";
    }

    // Save Attendance
    @PostMapping("/save")
    public String saveAttendance(@ModelAttribute("attendance") Attendance attendance) {

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

        return "attendance/attendance-form";
    }

    // Update Attendance
    @PostMapping("/update/{id}")
    public String updateAttendance(@PathVariable Long id,
            @ModelAttribute Attendance attendance) {

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

        if (keyword == null) {
            keyword = "";
        }

        model.addAttribute(
                "attendanceList",
                attendanceService.searchAttendance(keyword, attendanceDate));

        return "attendance/attendance-list";
    }

    // Pagination
    @GetMapping("/page/{pageNo}")
    public String findPaginated(
            @PathVariable int pageNo,
            Model model) {

        Page<Attendance> page = attendanceService.getAttendanceByPage(pageNo);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("attendanceList", page.getContent());

        return "attendance/attendance-list";
    }

}