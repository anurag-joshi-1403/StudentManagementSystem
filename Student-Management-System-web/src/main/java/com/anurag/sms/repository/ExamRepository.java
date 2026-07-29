package com.anurag.sms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByExamNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCaseOrExamDate(
            String examName,
            String subjectName,
            LocalDate examDate);

}