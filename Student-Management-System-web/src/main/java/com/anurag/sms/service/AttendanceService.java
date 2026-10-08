package com.anurag.sms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.dto.AttendanceSummary;
import com.anurag.sms.dto.BulkAttendanceForm;
import com.anurag.sms.entity.Attendance;
import com.anurag.sms.entity.Student;

public interface AttendanceService {

    // Display All Attendance
    List<Attendance> getAllAttendance();

    // Get Attendance By ID
    Attendance getAttendanceById(Long id);

    // Save Attendance
    Attendance saveAttendance(Attendance attendance);

    // Update Attendance
    Attendance updateAttendance(Attendance attendance);

    // Delete Attendance
    void deleteAttendance(Long id);

    // Search and pagination in one: keyword and date are each optional
    // (null or blank = no filter). pageNo starts at 1.
    Page<Attendance> searchAttendance(String keyword, LocalDate attendanceDate, int pageNo);

    // Dashboard Count
    long getTotalAttendance();

    // Dashboard: records in a given status ("Present" / "Absent" / "Late")
    long countByStatus(String status);

    // Student profile (E11): present out of all of this student's records
    AttendanceSummary getAttendanceSummary(Student student);

    // Attendance report (E12): one row per student; subjectId null = all subjects
    List<AttendanceSummary> getAttendanceReport(Long subjectId);

    // Bulk attendance (E14): one entry per student, in name order. Students
    // already recorded for this subject and date carry their status.
    List<BulkAttendanceForm.Entry> getBulkEntries(Long subjectId, LocalDate date);

    // Bulk attendance (E15): saves one record per entry and returns how many
    // were saved. Students already recorded for the subject and date are
    // skipped, so submitting the same class twice saves nothing new.
    int saveBulk(BulkAttendanceForm form);
}