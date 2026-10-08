package com.anurag.sms.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.dto.ImportReport;
import com.anurag.sms.entity.Student;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    Student saveStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudent(Long id);

    // One paged query for the list and for search: a blank keyword lists
    // every student. pageNo starts at 1.
    Page<Student> searchStudents(String keyword, int pageNo);

    boolean existsByEmail(String email);

    long getTotalStudents();

    long getMaleStudents();

    long getFemaleStudents();

    List<Student> getRecentStudents();

    // CSV import (E18): saves every valid row and reports the rest. A row
    // is valid if it passes the form's Bean Validation, its email is not
    // already used (in the database or earlier in the file) and its course
    // matches an existing course name.
    ImportReport importStudents(InputStream csv) throws IOException;
}
