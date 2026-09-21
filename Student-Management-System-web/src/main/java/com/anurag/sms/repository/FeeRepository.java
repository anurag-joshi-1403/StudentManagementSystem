package com.anurag.sms.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Fee;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    List<Fee> findByStudentFirstNameContainingIgnoreCaseOrFeeTypeContainingIgnoreCaseOrPaymentStatusContainingIgnoreCase(
            String studentName,
            String feeType,
            String paymentStatus);

    List<Fee> findTop5ByOrderByPaymentDateDesc();

    // Dashboard: total money collected, as opposed to the record count
    // returned by count(). COALESCE keeps an empty table at zero.
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fee f")
    BigDecimal sumAllAmounts();

    // Dashboard: "Paid" / "Pending" split
    long countByPaymentStatus(String paymentStatus);

    @Transactional
    @Modifying
    @Query("DELETE FROM Fee f WHERE f.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

}