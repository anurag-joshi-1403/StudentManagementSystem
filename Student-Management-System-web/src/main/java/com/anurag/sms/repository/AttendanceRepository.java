package com.anurag.sms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentFirstNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCaseOrAttendanceDate(
            String studentName,
            String subjectName,
            LocalDate attendanceDate);

    List<Attendance> findTop5ByOrderByAttendanceDateDesc();

    // Dashboard: "Present" / "Absent" / "Late" split
    long countByStatus(String status);


    @Transactional
    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.subject.id = :subjectId")
    void deleteBySubjectId(@Param("subjectId") Long subjectId);
}