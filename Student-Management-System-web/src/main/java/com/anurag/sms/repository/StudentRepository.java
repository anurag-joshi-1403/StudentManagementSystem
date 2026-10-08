package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            String course,
            Pageable pageable);

    boolean existsByEmail(String email);

    long countByGender(String gender);

    List<Student> findTop5ByOrderByIdDesc();
}
