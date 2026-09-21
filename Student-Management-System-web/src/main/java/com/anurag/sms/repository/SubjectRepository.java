package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.anurag.sms.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Search by Subject Name or Subject Code
    List<Subject> findBySubjectNameContainingIgnoreCaseOrSubjectCodeContainingIgnoreCase(
            String subjectName,
            String subjectCode);

    // Duplicate Subject Code Check
    boolean existsBySubjectCode(String subjectCode);

    // Dashboard: total credits across the whole catalogue
    @Query("SELECT COALESCE(SUM(s.credits), 0) FROM Subject s")
    long sumAllCredits();

}