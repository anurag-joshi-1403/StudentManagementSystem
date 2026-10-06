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

    // Keyword and date are each optional (null = no filter) and must BOTH
    // match. The derived OR-query this replaces ignored the date whenever
    // the keyword was blank, because Containing("") matches every row (#26).
    @Query("""
            SELECT a FROM Attendance a
            WHERE (:keyword IS NULL
                   OR LOWER(a.student.firstName)   LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(a.subject.subjectName) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:attendanceDate IS NULL OR a.attendanceDate = :attendanceDate)
            """)
    List<Attendance> search(@Param("keyword") String keyword,
                            @Param("attendanceDate") LocalDate attendanceDate);

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