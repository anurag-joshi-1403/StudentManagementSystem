package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Course;

public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(Long id);

    Course saveCourse(Course course);

    Course updateCourse(Course course);

    void deleteCourse(Long id);

    List<Course> searchCourses(String keyword);

    Page<Course> getCoursesByPage(int pageNo);

    boolean existsByCourseCode(String courseCode);

    long getTotalCourses();
}