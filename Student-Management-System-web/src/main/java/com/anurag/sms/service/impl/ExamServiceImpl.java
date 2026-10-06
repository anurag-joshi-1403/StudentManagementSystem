package com.anurag.sms.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Exam;
import com.anurag.sms.repository.ExamRepository;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.service.ExamService;

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
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
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
    public List<Exam> searchExam(String keyword, LocalDate examDate) {

        // A blank keyword means "no keyword filter", which the query
        // expresses as null. Passing "" would match every row (#26).
        String trimmed = (keyword == null || keyword.isBlank())
                ? null
                : keyword.trim();

        return examRepository.search(trimmed, examDate);
    }

    @Override
    public Page<Exam> getExamByPage(int page) {
        return examRepository.findAll(PageRequest.of(page - 1, 5));
    }

    @Override
    public long getTotalExams() {
        return examRepository.count();
    }

    @Override
    public long getUpcomingExamCount() {
        return examRepository.countByExamDateGreaterThanEqual(LocalDate.now());
    }

}