package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Enrollment;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.service.EnrollmentService;
import com.anurag.sms.utility.Pages;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

    @Override
    public boolean isAlreadyEnrolled(Long studentId, Long courseId, Long subjectId, Long ownId) {
        return ownId == null
                ? enrollmentRepository.existsByStudentIdAndCourseIdAndSubjectId(
                        studentId, courseId, subjectId)
                : enrollmentRepository.existsByStudentIdAndCourseIdAndSubjectIdAndIdNot(
                        studentId, courseId, subjectId, ownId);
    }

    @Override
    public Page<Enrollment> searchEnrollments(String keyword, int pageNo) {

        // Search used to return every match on one page (#11).
        if (keyword == null || keyword.isBlank()) {
            return enrollmentRepository.findAll(Pages.of(pageNo));
        }

        String k = keyword.trim();
        return enrollmentRepository
                .findByStudentFirstNameContainingIgnoreCaseOrCourseCourseNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCase(
                        k, k, k, Pages.of(pageNo));
    }

    @Override
    public long getTotalEnrollments() {
        return enrollmentRepository.count();
    }

    @Override
    public long getEnrolledStudentCount() {
        return enrollmentRepository.countDistinctStudents();
    }

}