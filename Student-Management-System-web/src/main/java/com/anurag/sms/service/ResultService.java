package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.dto.Marksheet;
import com.anurag.sms.entity.Result;

public interface ResultService {

    // Display All Results
    List<Result> getAllResults();

    // Get Result By ID
    Result getResultById(Long id);

    // Save Result
    Result saveResult(Result result);

    // Update Result
    Result updateResult(Result result);

    // Delete Result
    void deleteResult(Long id);

    // Search and pagination in one: a blank keyword lists every result.
    // pageNo starts at 1.
    Page<Result> searchResult(String keyword, int pageNo);

    // Dashboard Count
    long getTotalResults();

    // Exam detail page: the exam's results, highest marks first
    List<Result> getResultsByExam(Long examId);

    // Marksheet: the student's results with totals and an overall grade
    Marksheet getMarksheet(Long studentId);
}