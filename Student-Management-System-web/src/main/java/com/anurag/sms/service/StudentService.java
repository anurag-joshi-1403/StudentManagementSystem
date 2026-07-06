package com.anurag.sms.service;

import com.anurag.sms.entity.Student;
import java.util.List;
import java.util.Optional;
public interface StudentService {
    
    List<Student> getAllStudents();
    Optional<Student> getStudentById(Long id);
    Student saveStudent(Student student);
    Student updateStudent(Student student);
    void deleteStudent(Long id);
}
