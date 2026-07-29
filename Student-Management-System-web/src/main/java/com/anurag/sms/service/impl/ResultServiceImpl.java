package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Result;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.service.ResultService;

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
                .orElseThrow(() ->
                        new RuntimeException("Result not found with id : " + id));
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
    public List<Result> searchResult(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return resultRepository
                .findByStudentFirstNameContainingIgnoreCaseOrExamExamNameContainingIgnoreCaseOrGradeContainingIgnoreCaseOrResultStatusContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        keyword);
    }

    @Override
    public Page<Result> getResultByPage(int page) {

        return resultRepository.findAll(PageRequest.of(page - 1, 5));

    }

    @Override
    public long getTotalResults() {
        return resultRepository.count();
    }

    // ===============================
    // Grade & Result Calculation
    // ===============================

    private void calculateResult(Result result) {

        Exam exam = result.getExam();

        int obtainedMarks = result.getObtainedMarks();
        int totalMarks = exam.getTotalMarks();
        int passingMarks = exam.getPassingMarks();

        double percentage =
                ((double) obtainedMarks / totalMarks) * 100;

        // Result Status

        if (obtainedMarks >= passingMarks) {
            result.setResultStatus("Pass");
        } else {
            result.setResultStatus("Fail");
        }

        // Grade

        if (percentage >= 90) {
            result.setGrade("A+");
        } else if (percentage >= 80) {
            result.setGrade("A");
        } else if (percentage >= 70) {
            result.setGrade("B");
        } else if (percentage >= 60) {
            result.setGrade("C");
        } else if (percentage >= 50) {
            result.setGrade("D");
        } else {
            result.setGrade("F");
        }

    }

}