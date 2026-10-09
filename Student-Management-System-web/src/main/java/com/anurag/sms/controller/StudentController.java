package com.anurag.sms.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anurag.sms.dto.ImportReport;
import com.anurag.sms.entity.Student;
import com.anurag.sms.service.AttendanceService;
import com.anurag.sms.service.PdfService;
import com.anurag.sms.service.ResultService;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.utility.CsvHelper;
import com.anurag.sms.utility.FileUploadUtil;

import jakarta.validation.Valid;

@Controller
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    // Served back at /student-images/** by WebConfig
    private static final String PHOTO_DIR = "uploads/student-images";

    private final StudentService studentService;
    private final com.anurag.sms.service.CourseService courseService;
    private final ResultService resultService;
    private final AttendanceService attendanceService;
    private final PdfService pdfService;

    public StudentController(StudentService studentService,
                             com.anurag.sms.service.CourseService courseService,
                             ResultService resultService,
                             AttendanceService attendanceService,
                             PdfService pdfService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.resultService = resultService;
        this.attendanceService = attendanceService;
        this.pdfService = pdfService;
    }

    @GetMapping("/student")
    public String listStudents(Model model) {
        return searchStudents("", 1, model);
    }

    @GetMapping("/student/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {

        Student student = studentService.getStudentById(id);

        model.addAttribute("student", student);
        model.addAttribute("attendance", attendanceService.getAttendanceSummary(student));

        return LayoutView.render(model, "student/student-view :: content", "student", "Student Profile");
    }

    // Marksheet: every result for the student, with totals and an overall grade
    @GetMapping("/student/{id}/marksheet")
    public String marksheet(@PathVariable Long id, Model model) {

        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("marksheet", resultService.getMarksheet(id));

        return LayoutView.render(model, "student/marksheet :: content", "student", "Marksheet");
    }

    // The marksheet as a PDF (F7), with the same rows and totals as the page
    @GetMapping("/student/{id}/marksheet.pdf")
    public ResponseEntity<byte[]> marksheetPdf(@PathVariable Long id) {

        Student student = studentService.getStudentById(id);
        byte[] pdf = pdfService.marksheet(student, resultService.getMarksheet(id));

        return FileDownload.pdf("marksheet-" + slug(student.getFirstName() + " " + student.getLastName()) + ".pdf", pdf);
    }

    // "Kabir Mehta" -> "kabir-mehta", for a file name
    private static String slug(String text) {
        return text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    // CSV import (E18): the upload form, and the report of the last import
    @GetMapping("/student/import")
    public String importForm(Model model) {

        model.addAttribute("header", String.join(",", CsvHelper.STUDENT_HEADER));

        return LayoutView.render(model, "student/student-import :: content", "student", "Import Students");
    }

    // Saves the valid rows and comes back to the form with the report. A
    // redirect, so refreshing the page cannot import the file twice.
    @PostMapping("/student/import")
    public String importStudents(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirect) {

        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();

        if (file.isEmpty()) {
            redirect.addFlashAttribute("importError", "Choose a CSV file to import.");
        } else if (!name.toLowerCase(Locale.ROOT).endsWith(".csv")) {
            redirect.addFlashAttribute("importError", "Only .csv files can be imported.");
        } else {
            try (InputStream in = file.getInputStream()) {
                ImportReport report = studentService.importStudents(in);
                log.info("Student import: {}", report.summary());
                redirect.addFlashAttribute("report", report);
            } catch (IOException e) {
                log.error("Could not read the uploaded student CSV", e);
                redirect.addFlashAttribute("importError", "The file could not be read. Please try again.");
            }
        }
        return "redirect:/student/import";
    }

    // CSV export (E19), in the import format so the file can be imported
    // again. The byte order mark makes Excel read the file as UTF-8.
    @GetMapping("/student/export")
    public ResponseEntity<byte[]> exportStudents() {

        List<Student> students = new ArrayList<>(studentService.getAllStudents());
        students.sort(Comparator.comparing(Student::getId));

        return FileDownload.csv("students.csv", CsvHelper.studentsToCsv(students));
    }

    // The list and the search are one paged query: a blank keyword lists
    // everyone. The pager links back here with the keyword (#11).
    @GetMapping("/student/search")
    public String searchStudents(@RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        Page<Student> students = studentService.searchStudents(keyword, page);

        model.addAttribute("students", students.getContent());

        model.addAttribute("keyword", keyword);

        model.addAttribute("currentPage", students.getNumber() + 1);

        model.addAttribute("totalPages", students.getTotalPages());

        model.addAttribute("totalItems", students.getTotalElements());

        return LayoutView.render(model, "student/student-list :: content", "student", "Students");
    }

    @GetMapping("/student/new")
    public String createStudentForm(Model model) {
        Student student = new Student();

        model.addAttribute("student", student);
        model.addAttribute("courses", courseService.getAllCourses());
        return LayoutView.render(model, "student/student-form :: content", "student", "Add Student");
    }

    @PostMapping("/student")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            @RequestParam("photoFile") MultipartFile photoFile,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            return LayoutView.render(model, "student/student-form :: content", "student",
                    student.getId() == null ? "Add Student" : "Edit Student");
        }

        // Duplicate Email Validation (only flag if email belongs to another student)
        boolean emailClash = false;
        if (student.getId() == null) {
            emailClash = studentService.existsByEmail(student.getEmail());
        } else {
            Student existing = studentService.getStudentById(student.getId());
            if (existing != null && !existing.getEmail().equalsIgnoreCase(student.getEmail())) {
                emailClash = studentService.existsByEmail(student.getEmail());
            }
        }

        if (emailClash) {
            result.rejectValue(
                    "email",
                    "error.student",
                    "Email already exists");
            model.addAttribute("courses", courseService.getAllCourses());
            return LayoutView.render(model, "student/student-form :: content", "student",
                    student.getId() == null ? "Add Student" : "Edit Student");
        }

        // Read the current photo name BEFORE saving: the save merges the form
        // into the same managed entity, so afterwards it shows the new name.
        String oldPhoto = student.getId() == null
                ? null
                : studentService.getStudentById(student.getId()).getPhoto();

        if (photoFile.isEmpty()) {
            // No new file chosen: an edit keeps the existing photo
            student.setPhoto(oldPhoto);
        } else {
            try {
                student.setPhoto(FileUploadUtil.saveImage(photoFile, PHOTO_DIR));
            } catch (IllegalArgumentException e) {
                result.rejectValue("photo", "photo.invalid", e.getMessage());
                model.addAttribute("courses", courseService.getAllCourses());
                return LayoutView.render(model, "student/student-form :: content", "student",
                        student.getId() == null ? "Add Student" : "Edit Student");
            } catch (IOException e) {
                log.error("Could not save photo for student {}", student.getEmail(), e);
                result.rejectValue("photo", "photo.failed",
                        "The photo could not be saved, so nothing was changed. Please try again.");
                model.addAttribute("courses", courseService.getAllCourses());
                return LayoutView.render(model, "student/student-form :: content", "student",
                        student.getId() == null ? "Add Student" : "Edit Student");
            }
        }

        studentService.saveStudent(student);

        // Only once the record points at the new photo is the old file removed,
        // so a failed save never leaves the student without a photo (#7).
        if (oldPhoto != null && !oldPhoto.equals(student.getPhoto())) {
            deletePhoto(oldPhoto);
        }

        return "redirect:/student";
    }

    @GetMapping("/student/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("courses", courseService.getAllCourses());
        return LayoutView.render(model, "student/student-form :: content", "student", "Edit Student");
    }

    @PostMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        String photo = studentService.getStudentById(id).getPhoto();
        studentService.deleteStudent(id);
        deletePhoto(photo);
        return "redirect:/student";
    }

    // A leftover file is harmless, so a failed delete is logged, not shown.
    private void deletePhoto(String fileName) {
        try {
            FileUploadUtil.delete(PHOTO_DIR, fileName);
        } catch (IOException e) {
            log.warn("Could not delete old student photo {}", fileName, e);
        }
    }

    // Kept so existing /student/page/{n} links and bookmarks still work
    @GetMapping("/student/page/{pageNo}")
    public String findPaginated(
            @PathVariable int pageNo,
            Model model) {

        return searchStudents("", pageNo, model);
    }

}
