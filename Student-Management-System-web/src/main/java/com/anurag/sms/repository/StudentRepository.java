package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(
        String firstName,
        String lastName,
        String email,
        String course
    );
}
