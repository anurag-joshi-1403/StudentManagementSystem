package com.anurag.sms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Exam;

public interface ExamService {

    // Display All Exams
    List<Exam> getAllExams();

    // Get Exam By ID
    Exam getExamById(Long id);

    // Save Exam
    Exam saveExam(Exam exam);

    // Update Exam
    Exam updateExam(Exam exam);

    // Delete Exam
    void deleteExam(Long id);

    // Search Exam
    List<Exam> searchExam(String keyword, LocalDate examDate);

    // Pagination
    Page<Exam> getExamByPage(int page);

    // Dashboard Count
    long getTotalExams();

    // Dashboard: exams scheduled on or after today
    long getUpcomingExamCount();

    List<Exam> getUpcomingExams();
}