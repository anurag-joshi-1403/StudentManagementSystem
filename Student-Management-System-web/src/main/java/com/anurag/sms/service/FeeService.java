package com.anurag.sms.service;

import java.math.BigDecimal;
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

    // Dashboard Count (number of fee records)
    long getTotalFees();

    // Dashboard: total money collected across all fee records
    BigDecimal getTotalFeeAmount();

    // Dashboard: count of records in a given payment status
    long countByPaymentStatus(String paymentStatus);

    List<Fee> getRecentFees();

}