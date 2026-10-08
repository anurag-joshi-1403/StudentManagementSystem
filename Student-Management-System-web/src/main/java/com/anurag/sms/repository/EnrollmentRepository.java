package com.anurag.sms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Search by student first name, course name or subject name
    Page<Enrollment> findByStudentFirstNameContainingIgnoreCaseOrCourseCourseNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCase(
            String studentName,
            String courseName,
            String subjectName,
            Pageable pageable);

    // Duplicate check for a new enrollment, and for an edit (any OTHER row)
    boolean existsByStudentIdAndCourseIdAndSubjectId(Long studentId, Long courseId, Long subjectId);

    boolean existsByStudentIdAndCourseIdAndSubjectIdAndIdNot(
            Long studentId, Long courseId, Long subjectId, Long id);

    // Dashboard: how many individual students appear in the enrollment
    // table, as opposed to the total number of enrollment rows.
    @Query("SELECT COUNT(DISTINCT e.student.id) FROM Enrollment e")
    long countDistinctStudents();

    @Transactional
    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.subject.id = :subjectId")
    void deleteBySubjectId(@Param("subjectId") Long subjectId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);
}