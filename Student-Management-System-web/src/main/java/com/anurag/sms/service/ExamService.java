package com.anurag.sms.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

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

    // Search and pagination in one: keyword and date are each optional
    // (null or blank = no filter). pageNo starts at 1.
    Page<Exam> searchExam(String keyword, LocalDate examDate, int pageNo);

    // Dashboard Count
    long getTotalExams();

    // Dashboard: exams scheduled on or after today
    long getUpcomingExamCount();

    List<Exam> getUpcomingExams();

    // Subject detail page: the subject's exams in date order
    List<Exam> getExamsBySubject(Long subjectId);

    // Exam schedule: upcoming exams grouped by month, months in order and
    // exams in date order within each month
    Map<YearMonth, List<Exam>> getUpcomingSchedule();
}