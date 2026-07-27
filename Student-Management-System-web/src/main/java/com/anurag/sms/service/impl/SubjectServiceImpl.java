package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Subject;
import com.anurag.sms.repository.SubjectRepository;
import com.anurag.sms.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElseThrow();
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
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public List<Subject> searchSubjects(String keyword) {

        return subjectRepository
                .findBySubjectNameContainingIgnoreCaseOrSubjectCodeContainingIgnoreCase(
                        keyword,
                        keyword);
    }

    @Override
    public Page<Subject> getSubjectsByPage(int page) {

        return subjectRepository.findAll(
                PageRequest.of(page - 1, 5));
    }

    @Override
    public boolean existsBySubjectCode(String subjectCode) {
        return subjectRepository.existsBySubjectCode(subjectCode);
    }

    @Override
    public long getTotalSubjects() {
        return subjectRepository.count();
    }

}