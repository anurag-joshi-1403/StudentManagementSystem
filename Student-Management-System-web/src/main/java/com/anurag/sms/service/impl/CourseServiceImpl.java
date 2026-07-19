package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Course;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow();
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
    public void deleteCourse(Long id) {
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