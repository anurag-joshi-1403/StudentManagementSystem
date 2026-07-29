package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

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

    // Search Result
    List<Result> searchResult(String keyword);

    // Pagination
    Page<Result> getResultByPage(int page);

    // Dashboard Count
    long getTotalResults();
}