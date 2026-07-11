package com.anurag.sms.service;

import java.util.List;

import com.anurag.sms.entity.Student;
public interface StudentService {
    
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student saveStudent(Student student);
    Student updateStudent(Student student);
    void deleteStudent(Long id);
    List<Student> searchStudents(String keyword);
}
