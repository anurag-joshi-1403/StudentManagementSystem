package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anurag.sms.dto.Marksheet;
import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Result;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.service.ResultService;
import com.anurag.sms.utility.GradeCalculator;
import com.anurag.sms.utility.Pages;

@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    public ResultServiceImpl(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Override
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    @Override
    public Result getResultById(Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result", id));
    }

    @Override
    public Result saveResult(Result result) {

        calculateResult(result);

        return resultRepository.save(result);
    }

    @Override
    public Result updateResult(Result result) {

        calculateResult(result);

        return resultRepository.save(result);
    }

    @Override
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }

    @Override
    public Page<Result> searchResult(String keyword, int pageNo) {

        // Search used to return every match on one page (#11).
        if (keyword == null || keyword.isBlank()) {
            return resultRepository.findAll(Pages.of(pageNo));
        }

        String k = keyword.trim();
        return resultRepository
                .findByStudentFirstNameContainingIgnoreCaseOrExamExamNameContainingIgnoreCaseOrGradeContainingIgnoreCaseOrResultStatusContainingIgnoreCase(
                        k, k, k, k, Pages.of(pageNo));
    }

    @Override
    public long getTotalResults() {
        return resultRepository.count();
    }

    @Override
    public List<Result> getResultsByExam(Long examId) {
        return resultRepository.findByExamIdOrderByObtainedMarksDesc(examId);
    }

    @Override
    public Marksheet getMarksheet(Long studentId) {
        return Marksheet.of(resultRepository.findByStudentIdOrderByExamExamDateAsc(studentId));
    }

    // ===============================
    // Grade & Result Calculation
    // ===============================

    private void calculateResult(Result result) {

        Exam exam = result.getExam();

        int obtainedMarks = result.getObtainedMarks();

        // The rules live in GradeCalculator, shared with the marksheet
        boolean pass = GradeCalculator.isPass(obtainedMarks, exam.getPassingMarks());
        double percentage = GradeCalculator.percentage(obtainedMarks, exam.getTotalMarks());

        result.setResultStatus(GradeCalculator.status(pass));
        result.setGrade(GradeCalculator.grade(percentage, pass));
    }

}