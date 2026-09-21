package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Student;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.service.StudentService;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.EnrollmentRepository;
import com.anurag.sms.repository.FeeRepository;
import com.anurag.sms.repository.ResultRepository;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FeeRepository feeRepository;
    private final ResultRepository resultRepository;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            AttendanceRepository attendanceRepository,
            EnrollmentRepository enrollmentRepository,
            FeeRepository feeRepository,
            ResultRepository resultRepository) {

        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.feeRepository = feeRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public List<Student> getRecentStudents() {

        return studentRepository.findTop5ByOrderByIdDesc();

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
    @Transactional
    public void deleteStudent(Long id) {

        attendanceRepository.deleteByStudentId(id);

        enrollmentRepository.deleteByStudentId(id);

        feeRepository.deleteByStudentId(id);

        resultRepository.deleteByStudentId(id);

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
