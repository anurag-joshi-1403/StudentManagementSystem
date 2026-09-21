package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Subject;

public interface SubjectService {

    // Display All Subjects
    List<Subject> getAllSubjects();

    // Get Subject By ID
    Subject getSubjectById(Long id);

    // Save Subject
    Subject saveSubject(Subject subject);

    // Update Subject
    Subject updateSubject(Subject subject);

    // Delete Subject
    void deleteSubject(Long id);

    // Search Subject
    List<Subject> searchSubjects(String keyword);

    // Pagination
    Page<Subject> getSubjectsByPage(int page);

    // Duplicate Subject Code Check
    boolean existsBySubjectCode(String subjectCode);

    // Dashboard Count
    long getTotalSubjects();

    // Dashboard: total credits across the whole catalogue
    long getTotalCredits();

}