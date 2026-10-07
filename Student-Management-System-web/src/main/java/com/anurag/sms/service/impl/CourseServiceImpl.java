package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Course;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.service.CourseService;

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
    public List<Course> searchCourses(String keyword) {
        return courseRepository
                .findByCourseNameContainingIgnoreCaseOrCourseCodeContainingIgnoreCase(
                        keyword,
                        keyword);
    }

    @Override
    public Page<Course> getCoursesByPage(int pageNo) {

        PageRequest pageable = PageRequest.of(pageNo - 1, 5);

        return courseRepository.findAll(pageable);
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        return courseRepository.existsByCourseCode(courseCode);
    }

    @Override
    public long getTotalCourses() {
        return courseRepository.count();
    }
}