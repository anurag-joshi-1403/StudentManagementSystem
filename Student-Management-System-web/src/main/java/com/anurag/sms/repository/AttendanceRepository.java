package com.anurag.sms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.dto.AttendanceSummary;
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
    Page<Attendance> search(@Param("keyword") String keyword,
                            @Param("attendanceDate") LocalDate attendanceDate,
                            Pageable pageable);

    // Dashboard: "Present" / "Absent" / "Late" split
    long countByStatus(String status);

    // Bulk attendance (E14/E15): the records already taken for one class,
    // shown on the form, and the per-student check the save makes
    List<Attendance> findBySubjectIdAndAttendanceDate(Long subjectId, LocalDate attendanceDate);

    boolean existsByStudentIdAndSubjectIdAndAttendanceDate(Long studentId, Long subjectId, LocalDate attendanceDate);

    // Student profile (E11): all of a student's records, and those in one status
    long countByStudentId(Long studentId);

    long countByStudentIdAndStatus(Long studentId, String status);

    // Attendance report (E12): one row per student, present out of total,
    // optionally for one subject (null = all). Starts from Student with a
    // LEFT JOIN so students with no records still get a row (0 of 0).
    @Query("""
            SELECT new com.anurag.sms.dto.AttendanceSummary(
                       s.id, s.firstName, s.lastName,
                       SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END),
                       COUNT(a))
            FROM Student s
            LEFT JOIN Attendance a
                   ON a.student = s
                  AND (:subjectId IS NULL OR a.subject.id = :subjectId)
            GROUP BY s.id, s.firstName, s.lastName
            ORDER BY s.firstName, s.lastName
            """)
    List<AttendanceSummary> summarise(@Param("subjectId") Long subjectId);

    // Notifications (F1): only the students under the minimum, with the
    // same present-out-of-total rule as summarise(). An inner join, since a
    // student with no records has nothing to flag.
    @Query("""
            SELECT new com.anurag.sms.dto.AttendanceSummary(
                       s.id, s.firstName, s.lastName,
                       SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END),
                       COUNT(a))
            FROM Attendance a
            JOIN a.student s
            GROUP BY s.id, s.firstName, s.lastName
            HAVING SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(a) < :minimum
            ORDER BY s.firstName, s.lastName
            """)
    List<AttendanceSummary> findBelowMinimum(@Param("minimum") double minimum);

    @Transactional
    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.subject.id = :subjectId")
    void deleteBySubjectId(@Param("subjectId") Long subjectId);
}