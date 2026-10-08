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

    // One paged query for the list and for search: a blank keyword lists
    // every course. pageNo starts at 1.
    Page<Course> searchCourses(String keyword, int pageNo);

    // True when ANOTHER course already uses this code. ownId is the course
    // being edited, or null for a new one, so an edit may keep its own code.
    boolean isCourseCodeTaken(String courseCode, Long ownId);

    long getTotalCourses();
}