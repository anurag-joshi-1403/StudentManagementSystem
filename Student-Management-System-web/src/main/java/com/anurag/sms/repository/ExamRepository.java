package com.anurag.sms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByExamNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCaseOrExamDate(
            String examName,
            String subjectName,
            LocalDate examDate);

    List<Exam> findTop5ByOrderByExamDateAsc();

    // Dashboard: exams still ahead of us, rather than the whole table
    long countByExamDateGreaterThanEqual(LocalDate date);

    // The soonest exams that have not happened yet. The unfiltered
    // findTop5ByOrderByExamDateAsc above returns the OLDEST five rows,
    // which made the "Upcoming Exams" panel list past exams.
    List<Exam> findTop5ByExamDateGreaterThanEqualOrderByExamDateAsc(LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM Exam e WHERE e.subject.id = :subjectId")
    void deleteBySubjectId(@Param("subjectId") Long subjectId);
}
