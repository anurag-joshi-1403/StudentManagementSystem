package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.SubjectRepository;
import com.anurag.sms.service.CourseService;
import com.anurag.sms.utility.Pages;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public CourseServiceImpl(CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             SubjectRepository subjectRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
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
    @Transactional
    public Course saveCourse(Course course) {

        if (course.getId() == null) {
            return courseRepository.save(course);
        }

        // Copy the form's fields onto the stored course. Saving the posted
        // object itself would replace the curriculum with the form's empty
        // one and delete every course_subjects row (G9).
        Course stored = getCourseById(course.getId());
        stored.setCourseCode(course.getCourseCode());
        stored.setCourseName(course.getCourseName());
        stored.setDuration(course.getDuration());
        stored.setFees(course.getFees());
        stored.setDescription(course.getDescription());
        return courseRepository.save(stored);
    }

    @Override
    public Course updateCourse(Course course) {
        return saveCourse(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {

        // Every student belongs to a course (G8). Deleting theirs would
        // break that, and deleting the students with it would lose their
        // records, so the course stays until they are moved.
        long students = studentRepository.countByCourseId(id);
        if (students > 0) {
            throw new IllegalStateException(getCourseById(id).getCourseName() + " still has "
                    + students + (students == 1 ? " student" : " students")
                    + ". Move them to another course before deleting it.");
        }

        // Enrollments hold a NOT NULL foreign key to the course, so they
        // must be removed first or MySQL rejects the delete (#25). Its
        // course_subjects rows go with the course, which owns them.
        enrollmentRepository.deleteByCourseId(id);

        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addSubject(Long courseId, Long subjectId) {

        Course course = getCourseById(courseId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", subjectId));

        course.getSubjects().add(subject);
    }

    @Override
    @Transactional
    public void removeSubject(Long courseId, Long subjectId) {

        Course course = getCourseById(courseId);

        // Enrollments would otherwise pair the course with a subject it no
        // longer teaches
        long enrolled = enrollmentRepository.countByCourseIdAndSubjectId(courseId, subjectId);
        if (enrolled > 0) {
            throw new IllegalStateException(enrolled + (enrolled == 1 ? " enrollment uses" : " enrollments use")
                    + " this subject in " + course.getCourseName() + ". Remove "
                    + (enrolled == 1 ? "it" : "them") + " first.");
        }

        course.getSubjects().removeIf(subject -> subject.getId().equals(subjectId));
    }

    @Override
    public boolean offersSubject(Long courseId, Long subjectId) {
        return courseRepository.existsByIdAndSubjectsId(courseId, subjectId);
    }

    @Override
    public List<Course> getCoursesOffering(Long subjectId) {
        return courseRepository.findBySubjectsIdOrderByCourseName(subjectId);
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