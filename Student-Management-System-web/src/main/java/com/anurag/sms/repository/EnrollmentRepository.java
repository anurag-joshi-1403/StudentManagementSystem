package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Search by Student Name, Course Name or Subject Name
    List<Enrollment> findByStudentFirstNameContainingIgnoreCaseOrCourseCourseNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCase(
            String studentName,
            String courseName,
            String subjectName);

}