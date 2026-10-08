package com.anurag.sms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.anurag.sms.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Search by Subject Name or Subject Code
    Page<Subject> findBySubjectNameContainingIgnoreCaseOrSubjectCodeContainingIgnoreCase(
            String subjectName,
            String subjectCode,
            Pageable pageable);

    // Duplicate Subject Code Check
    boolean existsBySubjectCode(String subjectCode);

    // Edit check: does a DIFFERENT subject already use this code?
    boolean existsBySubjectCodeAndIdNot(String subjectCode, Long id);

    // Dashboard: total credits across the whole catalogue
    @Query("SELECT COALESCE(SUM(s.credits), 0) FROM Subject s")
    long sumAllCredits();

}