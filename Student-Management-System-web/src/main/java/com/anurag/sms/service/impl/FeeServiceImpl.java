package com.anurag.sms.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.ActivityLog;
import com.anurag.sms.entity.Fee;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.FeeRepository;
import com.anurag.sms.service.ActivityLogService;
import com.anurag.sms.service.FeeService;
import com.anurag.sms.utility.Pages;

@Service
public class FeeServiceImpl implements FeeService {

    private static final String PAID = "Paid";

    private final FeeRepository feeRepository;
    private final ActivityLogService activityLogService;

    public FeeServiceImpl(FeeRepository feeRepository,
                          ActivityLogService activityLogService) {
        this.feeRepository = feeRepository;
        this.activityLogService = activityLogService;
    }

    @Override
    public List<Fee> getRecentFees() {

        return feeRepository.findTop5ByOrderByPaymentDateDesc();

    }

    @Override
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    @Override
    public Fee getFeeById(Long id) {
        return feeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fee", id));
    }

    @Override
    public Fee saveFee(Fee fee) {

        Fee saved = feeRepository.save(fee);

        activityLogService.record(ActivityLog.CREATED, "Fee", describe(saved));
        return saved;
    }

    @Override
    public Fee updateFee(Fee fee) {

        Fee saved = feeRepository.save(fee);

        activityLogService.record(ActivityLog.UPDATED, "Fee", describe(saved));
        return saved;
    }

    @Override
    public void deleteFee(Long id) {

        // Read the details first: afterwards there is nothing left to describe.
        // An unknown id deletes nothing, so it is not logged either.
        Fee fee = feeRepository.findById(id).orElse(null);
        if (fee == null) {
            return;
        }
        String description = describe(fee);

        feeRepository.deleteById(id);

        activityLogService.record(ActivityLog.DELETED, "Fee", description);
    }

    // e.g. "Tuition fee for Diya Patel (Pending)"
    private static String describe(Fee fee) {
        String student = fee.getStudent() == null ? "a student"
                : fee.getStudent().getFirstName() + " " + fee.getStudent().getLastName();
        return fee.getFeeType() + " fee for " + student + " (" + fee.getPaymentStatus() + ")";
    }

    @Override
    public Page<Fee> searchFee(String keyword, int pageNo) {

        // Search used to return every match on one page (#11).
        if (keyword == null || keyword.isBlank()) {
            return feeRepository.findAll(Pages.of(pageNo));
        }

        String k = keyword.trim();
        return feeRepository
                .findByStudentFirstNameContainingIgnoreCaseOrFeeTypeContainingIgnoreCaseOrPaymentStatusContainingIgnoreCase(
                        k, k, k, Pages.of(pageNo));
    }

    @Override
    public long getTotalFees() {
        return feeRepository.count();
    }

    @Override
    public BigDecimal getTotalFeeAmount() {

        BigDecimal total = feeRepository.sumAllAmounts();

        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public long countByPaymentStatus(String paymentStatus) {
        return feeRepository.countByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Fee> getOverdueFees() {
        return feeRepository.findByPaymentStatusNotAndDueDateBefore(PAID, LocalDate.now());
    }

    @Override
    public long getOverdueFeeCount() {
        return feeRepository.countByPaymentStatusNotAndDueDateBefore(PAID, LocalDate.now());
    }

}