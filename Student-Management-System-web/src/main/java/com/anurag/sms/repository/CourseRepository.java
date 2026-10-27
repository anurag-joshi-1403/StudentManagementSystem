package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByCourseNameContainingIgnoreCaseOrCourseCodeContainingIgnoreCase(
            String courseName,
            String courseCode,
            Pageable pageable);

    boolean existsByCourseCode(String courseCode);

    // Edit check: does a DIFFERENT course already use this code?
    boolean existsByCourseCodeAndIdNot(String courseCode, Long id);

    // Curriculum (G9): is this subject taught in this course?
    boolean existsByIdAndSubjectsId(Long courseId, Long subjectId);

    // Subject page: the courses that teach it
    List<Course> findBySubjectsIdOrderByCourseName(Long subjectId);

    // Deleting a subject takes it out of every curriculum first. Course owns
    // the link, so nothing else would clear these rows.
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM course_subjects WHERE subject_id = :subjectId", nativeQuery = true)
    void removeSubjectFromCourses(@Param("subjectId") Long subjectId);
}