package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Attendance;
import com.anurag.sms.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Existing search method
    List<Enrollment> findByStudentFirstNameContainingIgnoreCaseOrCourseCourseNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCase(
            String studentName,
            String courseName,
            String subjectName);

    List<Attendance> findTop5ByOrderByAttendanceDateDesc();

    @Transactional
    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.subject.id = :subjectId")
    void deleteBySubjectId(@Param("subjectId") Long subjectId);
}