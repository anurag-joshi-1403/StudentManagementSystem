package com.anurag.sms.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Exam;
import com.anurag.sms.repository.ExamRepository;
import com.anurag.sms.service.ExamService;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public List<Exam> getUpcomingExams() {

        return examRepository.findTop5ByOrderByExamDateAsc();

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
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    @Override
    public List<Exam> searchExam(String keyword, LocalDate examDate) {

        if (keyword == null) {
            keyword = "";
        }

        if (examDate == null) {
            examDate = LocalDate.of(1900, 1, 1);
        }

        return examRepository
                .findByExamNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCaseOrExamDate(
                        keyword,
                        keyword,
                        examDate);
    }

    @Override
    public Page<Exam> getExamByPage(int page) {
        return examRepository.findAll(PageRequest.of(page - 1, 5));
    }

    @Override
    public long getTotalExams() {
        return examRepository.count();
    }

}