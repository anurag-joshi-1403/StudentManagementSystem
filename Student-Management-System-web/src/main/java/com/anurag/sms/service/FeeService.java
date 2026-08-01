package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Fee;

public interface FeeService {

    // Display All Fees
    List<Fee> getAllFees();

    // Get Fee By ID
    Fee getFeeById(Long id);

    // Save Fee
    Fee saveFee(Fee fee);

    // Update Fee
    Fee updateFee(Fee fee);

    // Delete Fee
    void deleteFee(Long id);

    // Search Fee
    List<Fee> searchFee(String keyword);

    // Pagination
    Page<Fee> getFeeByPage(int page);

    // Dashboard Count
    long getTotalFees();

    List<Fee> getRecentFees();

}