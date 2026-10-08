package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Enrollment;

public interface EnrollmentService {

    // Display All Enrollments
    List<Enrollment> getAllEnrollments();

    // Get Enrollment By ID
    Enrollment getEnrollmentById(Long id);

    // Save Enrollment
    Enrollment saveEnrollment(Enrollment enrollment);

    // Update Enrollment
    Enrollment updateEnrollment(Enrollment enrollment);

    // Delete Enrollment
    void deleteEnrollment(Long id);

    // True when the student is already enrolled in this course and subject.
    // ownId is the enrollment being edited, or null for a new one.
    boolean isAlreadyEnrolled(Long studentId, Long courseId, Long subjectId, Long ownId);

    // Search and pagination in one: a blank keyword lists every enrollment.
    // pageNo starts at 1.
    Page<Enrollment> searchEnrollments(String keyword, int pageNo);

    // Dashboard Count
    long getTotalEnrollments();

    // Dashboard: distinct students appearing in the enrollment table
    long getEnrolledStudentCount();

}