package com.anurag.sms.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Exam;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.ExamRepository;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.service.ExamService;
import com.anurag.sms.utility.Pages;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ResultRepository resultRepository;

    public ExamServiceImpl(ExamRepository examRepository,
                           ResultRepository resultRepository) {
        this.examRepository = examRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public List<Exam> getUpcomingExams() {

        // Must agree with getUpcomingExamCount(): without the date filter
        // the dashboard showed "0 upcoming" on the stat card while the
        // panel beneath it listed exams that had already happened.
        return examRepository
                .findTop5ByExamDateGreaterThanEqualOrderByExamDateAsc(LocalDate.now());
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", id));
    }

    @Override
    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Exam updateExam(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    @Transactional
    public void deleteExam(Long id) {

        // Results hold a NOT NULL foreign key to the exam, so they must be
        // removed first or MySQL rejects the delete (#25).
        resultRepository.deleteByExamId(id);

        examRepository.deleteById(id);
    }

    @Override
    public Page<Exam> searchExam(String keyword, LocalDate examDate, int pageNo) {

        // A blank keyword means "no keyword filter", which the query
        // expresses as null. Passing "" would match every row (#26).
        String trimmed = (keyword == null || keyword.isBlank())
                ? null
                : keyword.trim();

        // Search used to return every match on one page (#11).
        return examRepository.search(trimmed, examDate, Pages.of(pageNo));
    }

    @Override
    public long getTotalExams() {
        return examRepository.count();
    }

    @Override
    public long getUpcomingExamCount() {
        return examRepository.countByExamDateGreaterThanEqual(LocalDate.now());
    }

    @Override
    public List<Exam> getExamsBySubject(Long subjectId) {
        return examRepository.findBySubjectIdOrderByExamDateAsc(subjectId);
    }

    @Override
    public Map<YearMonth, List<Exam>> getUpcomingSchedule() {

        // TreeMap keeps the months in calendar order. The query already
        // sorts by date, so each month's list comes out in date order.
        Map<YearMonth, List<Exam>> schedule = new TreeMap<>();

        for (Exam exam : examRepository
                .findByExamDateGreaterThanEqualOrderByExamDateAscExamNameAsc(LocalDate.now())) {

            schedule.computeIfAbsent(YearMonth.from(exam.getExamDate()), month -> new ArrayList<>())
                    .add(exam);
        }

        return schedule;
    }

}