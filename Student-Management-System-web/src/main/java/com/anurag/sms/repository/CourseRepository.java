package com.anurag.sms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByCourseNameContainingIgnoreCaseOrCourseCodeContainingIgnoreCase(
            String courseName,
            String courseCode,
            Pageable pageable);

    boolean existsByCourseCode(String courseCode);

    // Edit check: does a DIFFERENT course already use this code?
    boolean existsByCourseCodeAndIdNot(String courseCode, Long id);
}