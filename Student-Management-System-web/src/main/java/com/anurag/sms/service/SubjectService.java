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

    // Search and pagination in one: a blank keyword lists every subject.
    // pageNo starts at 1.
    Page<Subject> searchSubjects(String keyword, int pageNo);

    // Duplicate check: true when ANOTHER subject already uses this code.
    // ownId is the subject being edited, or null for a new one.
    boolean isSubjectCodeTaken(String subjectCode, Long ownId);

    // Dashboard Count
    long getTotalSubjects();

    // Dashboard: total credits across the whole catalogue
    long getTotalCredits();

}