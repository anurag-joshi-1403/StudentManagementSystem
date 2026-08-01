package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Student;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    Student saveStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudent(Long id);

    List<Student> searchStudents(String keyword);

    Page<Student> getStudentsByPage(int pageNo);

    boolean existsByEmail(String email);

    long getTotalStudents();

    long getMaleStudents();

    long getFemaleStudents();

    public Object getRecentStudents();
}
