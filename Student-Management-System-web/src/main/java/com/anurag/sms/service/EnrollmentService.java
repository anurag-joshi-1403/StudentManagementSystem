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

    // Search Enrollment
    List<Enrollment> searchEnrollments(String keyword);

    // Pagination
    Page<Enrollment> getEnrollmentsByPage(int page);

    // Dashboard Count
    long getTotalEnrollments();

}