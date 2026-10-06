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

                long totalStudents = studentService.getTotalStudents();
                long maleStudents = studentService.getMaleStudents();
                long femaleStudents = studentService.getFemaleStudents();

                model.addAttribute("totalStudents", totalStudents);
                model.addAttribute("maleStudents", maleStudents);
                model.addAttribute("femaleStudents", femaleStudents);

                // The student form also offers "Other", and a record can predate
                // the gender field entirely. Deriving the remainder keeps the
                // gender chart reconciled with the headline student count
                // instead of silently under-reporting.
                model.addAttribute("otherStudents",
                                Math.max(0, totalStudents - maleStudents - femaleStudents));

                model.addAttribute("totalTeachers", teacherService.getTotalTeachers());
                model.addAttribute("totalCourses", courseService.getTotalCourses());
                model.addAttribute("totalSubjects", subjectService.getTotalSubjects());
                model.addAttribute("totalFees", feeService.getTotalFees());
                model.addAttribute("totalEnrollments", enrollmentService.getTotalEnrollments());
                model.addAttribute("totalAttendance", attendanceService.getTotalAttendance());
                model.addAttribute("totalExams", examService.getTotalExams());

                // Supporting facts shown beneath each stat card. These replace
                // the hardcoded "12% this month" figures, which were computed
                // from nothing.
                model.addAttribute("departmentCount", teacherService.getDepartmentCount());
                model.addAttribute("totalCredits", subjectService.getTotalCredits());
                model.addAttribute("totalFeeAmount", feeService.getTotalFeeAmount());
                model.addAttribute("paidFees", feeService.countByPaymentStatus("Paid"));
                model.addAttribute("pendingFees", feeService.countByPaymentStatus("Pending"));
                model.addAttribute("enrolledStudents", enrollmentService.getEnrolledStudentCount());
                model.addAttribute("presentCount", attendanceService.countByStatus("Present"));
                model.addAttribute("upcomingExamCount", examService.getUpcomingExamCount());

                model.addAttribute("recentStudents",
                                studentService.getRecentStudents());

                model.addAttribute("recentFees",
                                feeService.getRecentFees());

                model.addAttribute("upcomingExams",
                                examService.getUpcomingExams());

                return LayoutView.render(model,
                                "dashboard/dashboard-content :: dashboardContent",
                                "dashboard", "Dashboard");
        }
}