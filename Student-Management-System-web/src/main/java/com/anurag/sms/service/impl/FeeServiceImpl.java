package com.anurag.sms.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Fee;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.FeeRepository;
import com.anurag.sms.service.FeeService;

@Service
public class FeeServiceImpl implements FeeService {

    private final FeeRepository feeRepository;

    public FeeServiceImpl(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
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
        return feeRepository.save(fee);
    }

    @Override
    public Fee updateFee(Fee fee) {
        return feeRepository.save(fee);
    }

    @Override
    public void deleteFee(Long id) {
        feeRepository.deleteById(id);
    }

    @Override
    public List<Fee> searchFee(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return feeRepository
                .findByStudentFirstNameContainingIgnoreCaseOrFeeTypeContainingIgnoreCaseOrPaymentStatusContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword);
    }

    @Override
    public Page<Fee> getFeeByPage(int page) {

        return feeRepository.findAll(PageRequest.of(page - 1, 5));

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

}