package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Enrollment;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.service.EnrollmentService;

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
    public List<Enrollment> searchEnrollments(String keyword) {

        return enrollmentRepository
                .findByStudentFirstNameContainingIgnoreCaseOrCourseCourseNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword);
    }

    @Override
    public Page<Enrollment> getEnrollmentsByPage(int page) {

        return enrollmentRepository.findAll(
                PageRequest.of(page - 1, 5));
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