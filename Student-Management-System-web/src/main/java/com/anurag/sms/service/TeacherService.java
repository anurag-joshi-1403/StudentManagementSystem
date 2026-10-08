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

    // One paged query for the list and for search: a blank keyword lists
    // every teacher. pageNo starts at 1.
    Page<Teacher> searchTeachers(String keyword, int pageNo);

    // True when ANOTHER teacher already uses this email. ownId is the teacher
    // being edited, or null for a new one, so an edit may keep its own email.
    boolean isEmailTaken(String email, Long ownId);

    long getTotalTeachers();

    // Dashboard: how many distinct departments the faculty spans
    long getDepartmentCount();

}