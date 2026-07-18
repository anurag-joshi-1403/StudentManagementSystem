package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Student;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> searchStudents(String keyword) {
        return studentRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        keyword);
    }

    @Override
    public Page<Student> getStudentsByPage(int pageNo) {

        PageRequest pageable = PageRequest.of(pageNo - 1, 5);

        return studentRepository.findAll(pageable);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public long getTotalStudents() {
        return studentRepository.count();
    }

    @Override
    public long getMaleStudents() {
        return studentRepository.countByGender("Male");
    }

    @Override
    public long getFemaleStudents() {
        return studentRepository.countByGender("Female");
    }
}
