package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {

    // Search by student first name, exam name, grade or pass/fail status
    Page<Result> findByStudentFirstNameContainingIgnoreCaseOrExamExamNameContainingIgnoreCaseOrGradeContainingIgnoreCaseOrResultStatusContainingIgnoreCase(
            String studentName,
            String examName,
            String grade,
            String resultStatus,
            Pageable pageable);

    // Exam detail page: every result for one exam, top marks first
    List<Result> findByExamIdOrderByObtainedMarksDesc(Long examId);

    // Marksheet: every result for one student, in exam-date order
    List<Result> findByStudentIdOrderByExamExamDateAsc(Long studentId);

    // Add this
    @Transactional
    @Modifying
    @Query("DELETE FROM Result r WHERE r.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Result r WHERE r.exam.id = :examId")
    void deleteByExamId(@Param("examId") Long examId);

    // Bulk JPQL deletes cannot join through r.exam.subject, so the
    // subject's exams are matched with a subquery instead.
    @Transactional
    @Modifying
    @Query("DELETE FROM Result r WHERE r.exam.id IN "
            + "(SELECT e.id FROM Exam e WHERE e.subject.id = :subjectId)")
    void deleteByExamSubjectId(@Param("subjectId") Long subjectId);
}