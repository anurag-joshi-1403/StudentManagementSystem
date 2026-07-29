package com.anurag.sms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Search by Student Name, Subject Name or Attendance Date
    List<Attendance> findByStudentFirstNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCaseOrAttendanceDate(
            String studentName,
            String subjectName,
            LocalDate attendanceDate);

}