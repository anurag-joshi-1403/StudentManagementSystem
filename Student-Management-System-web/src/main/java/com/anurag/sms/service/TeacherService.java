package com.anurag.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anurag.sms.entity.Teacher;

public interface TeacherService {

    List<Teacher> getAllTeachers();

    Teacher getTeacherById(Long id);

    Teacher saveTeacher(Teacher teacher);

    Teacher updateTeacher(Teacher teacher);

    void deleteTeacher(Long id);

    List<Teacher> searchTeachers(String keyword);

    Page<Teacher> getTeachersByPage(int pageNo);

    boolean existsByEmail(String email);

    long getTotalTeachers();

    // Dashboard: how many distinct departments the faculty spans
    long getDepartmentCount();

}