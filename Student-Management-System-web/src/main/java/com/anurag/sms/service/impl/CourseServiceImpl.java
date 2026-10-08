package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Course;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.service.CourseService;
import com.anurag.sms.utility.Pages;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseServiceImpl(CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {

        // Enrollments hold a NOT NULL foreign key to the course, so they
        // must be removed first or MySQL rejects the delete (#25).
        enrollmentRepository.deleteByCourseId(id);

        courseRepository.deleteById(id);
    }

    @Override
    public Page<Course> searchCourses(String keyword, int pageNo) {

        // Search used to return every match on one page (#11).
        if (keyword == null || keyword.isBlank()) {
            return courseRepository.findAll(Pages.of(pageNo));
        }

        String k = keyword.trim();
        return courseRepository
                .findByCourseNameContainingIgnoreCaseOrCourseCodeContainingIgnoreCase(
                        k, k, Pages.of(pageNo));
    }

    @Override
    public boolean isCourseCodeTaken(String courseCode, Long ownId) {
        return ownId == null
                ? courseRepository.existsByCourseCode(courseCode)
                : courseRepository.existsByCourseCodeAndIdNot(courseCode, ownId);
    }

    @Override
    public long getTotalCourses() {
        return courseRepository.count();
    }
}