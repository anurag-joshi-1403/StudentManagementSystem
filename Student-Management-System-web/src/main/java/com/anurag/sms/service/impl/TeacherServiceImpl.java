package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Teacher;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.TeacherRepository;
import com.anurag.sms.service.TeacherService;
import com.anurag.sms.utility.Pages;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", id));
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher updateTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public Page<Teacher> searchTeachers(String keyword, int pageNo) {

        // Search used to return every match on one page (#11).
        if (keyword == null || keyword.isBlank()) {
            return teacherRepository.findAll(Pages.of(pageNo));
        }

        String k = keyword.trim();
        return teacherRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCase(
                        k, k, k, k, Pages.of(pageNo));
    }

    @Override
    public boolean isEmailTaken(String email, Long ownId) {
        return ownId == null
                ? teacherRepository.existsByEmail(email)
                : teacherRepository.existsByEmailAndIdNot(email, ownId);
    }

    @Override
    public long getTotalTeachers() {
        return teacherRepository.count();
    }

    @Override
    public long getDepartmentCount() {
        return teacherRepository.countDistinctDepartments();
    }

}