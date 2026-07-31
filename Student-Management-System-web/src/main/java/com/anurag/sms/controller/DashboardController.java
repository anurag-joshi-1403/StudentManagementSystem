package com.anurag.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.anurag.sms.service.AttendanceService;
import com.anurag.sms.service.CourseService;
import com.anurag.sms.service.EnrollmentService;
import com.anurag.sms.service.ExamService;
import com.anurag.sms.service.FeeService;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.service.SubjectService;
import com.anurag.sms.service.TeacherService;

@Controller
public class DashboardController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final SubjectService subjectService;
    private final FeeService feeService;
    private final EnrollmentService enrollmentService;
    private final AttendanceService attendanceService;
    private final ExamService examService;

    public DashboardController(
            StudentService studentService,
            TeacherService teacherService,
            CourseService courseService,
            SubjectService subjectService,
            FeeService feeService,
            EnrollmentService enrollmentService,
            AttendanceService attendanceService,
            ExamService examService) {

        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.subjectService = subjectService;
        this.feeService = feeService;
        this.enrollmentService = enrollmentService;
        this.attendanceService = attendanceService;
        this.examService = examService;

        System.out.println("===== DashboardController Loaded =====");
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalStudents", studentService.getTotalStudents());
        model.addAttribute("maleStudents", studentService.getMaleStudents());
        model.addAttribute("femaleStudents", studentService.getFemaleStudents());

        model.addAttribute("totalTeachers", teacherService.getTotalTeachers());
        model.addAttribute("totalCourses", courseService.getTotalCourses());
        model.addAttribute("totalSubjects", subjectService.getTotalSubjects());
        model.addAttribute("totalFees", feeService.getTotalFees());
        model.addAttribute("totalEnrollments", enrollmentService.getTotalEnrollments());
        model.addAttribute("totalAttendance", attendanceService.getTotalAttendance());
        model.addAttribute("totalExams", examService.getTotalExams());

        return "dashboard";
    }
}