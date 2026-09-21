package com.anurag.sms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Attendance;

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

    // Search Attendance
    List<Attendance> searchAttendance(String keyword, LocalDate attendanceDate);

    // Pagination
    Page<Attendance> getAttendanceByPage(int page);

    // Dashboard Count
    long getTotalAttendance();

    // Dashboard: records in a given status ("Present" / "Absent" / "Late")
    long countByStatus(String status);
}