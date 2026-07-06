package com.anurag.sms.service.impl;

import com.anurag.sms.entity.Student;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.service.StudentService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    @
}
