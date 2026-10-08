package com.anurag.sms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.anurag.sms.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Name, email or department, so "Physics" finds the whole department
    Page<Teacher> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            String department,
            Pageable pageable);

    boolean existsByEmail(String email);

    // Edit check: does a DIFFERENT teacher already use this email?
    boolean existsByEmailAndIdNot(String email, Long id);

    // Dashboard: how many distinct departments the faculty spans
    @Query("SELECT COUNT(DISTINCT t.department) FROM Teacher t WHERE t.department IS NOT NULL")
    long countDistinctDepartments();

}