package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByStudentFirstNameContainingIgnoreCaseOrExamExamNameContainingIgnoreCaseOrGradeContainingIgnoreCaseOrResultStatusContainingIgnoreCase(
            String studentName,
            String examName,
            String grade,
            String resultStatus);

            void deleteByStudentId(Long studentId);
}