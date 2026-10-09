package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anurag.sms.dto.Marksheet;
import com.anurag.sms.entity.ActivityLog;
import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Result;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.service.ActivityLogService;
import com.anurag.sms.service.ResultService;
import com.anurag.sms.utility.GradeCalculator;
import com.anurag.sms.utility.Pages;

@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ActivityLogService activityLogService;

    public ResultServiceImpl(ResultRepository resultRepository,
                             ActivityLogService activityLogService) {
        this.resultRepository = resultRepository;
        this.activityLogService = activityLogService;
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

        Result saved = resultRepository.save(result);

        activityLogService.record(ActivityLog.CREATED, "Result", describe(saved));
        return saved;
    }

    @Override
    public Result updateResult(Result result) {

        calculateResult(result);

        Result saved = resultRepository.save(result);

        activityLogService.record(ActivityLog.UPDATED, "Result", describe(saved));
        return saved;
    }

    @Override
    public void deleteResult(Long id) {

        // Read the details first: afterwards there is nothing left to describe.
        // An unknown id deletes nothing, so it is not logged either.
        Result result = resultRepository.findById(id).orElse(null);
        if (result == null) {
            return;
        }
        String description = describe(result);

        resultRepository.deleteById(id);

        activityLogService.record(ActivityLog.DELETED, "Result", description);
    }

    // e.g. "Mid sem: Kabir Mehta, 400/1000 (D, Pass)"
    private static String describe(Result result) {
        return result.getExam().getExamName() + ": "
                + result.getStudent().getFirstName() + " " + result.getStudent().getLastName() + ", "
                + result.getObtainedMarks() + "/" + result.getExam().getTotalMarks()
                + " (" + result.getGrade() + ", " + result.getResultStatus() + ")";
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