package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Course;

public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(Long id);

    // Saves the course's own fields. An edit keeps the course's subjects,
    // which the form does not carry.
    Course saveCourse(Course course);

    Course updateCourse(Course course);

    // Throws IllegalStateException while students belong to the course (G8)
    void deleteCourse(Long id);

    // Curriculum (G9), managed from the course page
    void addSubject(Long courseId, Long subjectId);

    // Throws IllegalStateException while students are enrolled in the
    // subject through this course
    void removeSubject(Long courseId, Long subjectId);

    boolean offersSubject(Long courseId, Long subjectId);

    List<Course> getCoursesOffering(Long subjectId);

    // One paged query for the list and for search: a blank keyword lists
    // every course. pageNo starts at 1.
    Page<Course> searchCourses(String keyword, int pageNo);

    // True when ANOTHER course already uses this code. ownId is the course
    // being edited, or null for a new one, so an edit may keep its own code.
    boolean isCourseCodeTaken(String courseCode, Long ownId);

    long getTotalCourses();
}