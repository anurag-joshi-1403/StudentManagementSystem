package com.anurag.sms.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Fee;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    Page<Fee> findByStudentFirstNameContainingIgnoreCaseOrFeeTypeContainingIgnoreCaseOrPaymentStatusContainingIgnoreCase(
            String studentName,
            String feeType,
            String paymentStatus,
            Pageable pageable);

    List<Fee> findTop5ByOrderByPaymentDateDesc();

    // Dashboard: total money collected, as opposed to the record count
    // returned by count(). COALESCE keeps an empty table at zero.
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fee f")
    BigDecimal sumAllAmounts();

    // Dashboard: "Paid" / "Pending" split
    long countByPaymentStatus(String paymentStatus);

    // Overdue: not paid and past the due date, the rule in Fee.isOverdue().
    // Called with ("Paid", today).
    List<Fee> findByPaymentStatusNotAndDueDateBefore(String paidStatus, LocalDate today);

    long countByPaymentStatusNotAndDueDateBefore(String paidStatus, LocalDate today);

    @Transactional
    @Modifying
    @Query("DELETE FROM Fee f WHERE f.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

}