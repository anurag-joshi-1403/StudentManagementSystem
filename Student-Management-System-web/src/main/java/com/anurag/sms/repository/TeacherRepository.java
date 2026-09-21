package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.anurag.sms.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName);

    boolean existsByEmail(String email);

    // Dashboard: how many distinct departments the faculty spans
    @Query("SELECT COUNT(DISTINCT t.department) FROM Teacher t WHERE t.department IS NOT NULL")
    long countDistinctDepartments();

}