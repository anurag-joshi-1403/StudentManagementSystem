package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourseNameContainingIgnoreCaseOrCourseCodeContainingIgnoreCase(
            String courseName,
            String courseCode);

    boolean existsByCourseCode(String courseCode);
}