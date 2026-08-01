package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Fee;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    List<Fee> findByStudentFirstNameContainingIgnoreCaseOrFeeTypeContainingIgnoreCaseOrPaymentStatusContainingIgnoreCase(
            String studentName,
            String feeType,
            String paymentStatus);

    List<Fee> findTop5ByOrderByPaymentDateDesc();

}