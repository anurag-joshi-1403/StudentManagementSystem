package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Subject;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.repository.ExamRepository;
import com.anurag.sms.repository.ResultRepository;
import com.anurag.sms.repository.SubjectRepository;
import com.anurag.sms.service.SubjectService;
import com.anurag.sms.utility.Pages;
import org.springframework.transaction.annotation.Transactional;

// import com.anurag.sms.repository.AttendanceRepository;
// import com.anurag.sms.repository.EnrollmentRepository;
// import com.anurag.sms.repository.ExamRepository;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ExamRepository examRepository;
    private final ResultRepository resultRepository;

    public SubjectServiceImpl(
            SubjectRepository subjectRepository,
            AttendanceRepository attendanceRepository,
            EnrollmentRepository enrollmentRepository,
            ExamRepository examRepository,
            ResultRepository resultRepository) {

        this.subjectRepository = subjectRepository;
        this.attendanceRepository = attendanceRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.examRepository = examRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", id));
    }

    @Override
    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {

        attendanceRepository.deleteBySubjectId(id);

        enrollmentRepository.deleteBySubjectId(id);

        // Results point at this subject's exams, so they must go before
        // the exams themselves or MySQL rejects the delete (#25).
        resultRepository.deleteByExamSubjectId(id);

        examRepository.deleteBySubjectId(id);

        subjectRepository.deleteById(id);
    }

    @Override
    public Page<Subject> searchSubjects(String keyword, int pageNo) {

        // Search used to return every match on one page (#11).
        if (keyword == null || keyword.isBlank()) {
            return subjectRepository.findAll(Pages.of(pageNo));
        }

        String k = keyword.trim();
        return subjectRepository
                .findBySubjectNameContainingIgnoreCaseOrSubjectCodeContainingIgnoreCase(
                        k, k, Pages.of(pageNo));
    }

    @Override
    public boolean isSubjectCodeTaken(String subjectCode, Long ownId) {
        return ownId == null
                ? subjectRepository.existsBySubjectCode(subjectCode)
                : subjectRepository.existsBySubjectCodeAndIdNot(subjectCode, ownId);
    }

    @Override
    public long getTotalSubjects() {
        return subjectRepository.count();
    }

    @Override
    public long getTotalCredits() {
        return subjectRepository.sumAllCredits();
    }

}