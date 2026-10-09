package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.ActivityLog;
import com.anurag.sms.entity.Teacher;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.TeacherRepository;
import com.anurag.sms.service.ActivityLogService;
import com.anurag.sms.service.TeacherService;
import com.anurag.sms.utility.Pages;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final ActivityLogService activityLogService;

    public TeacherServiceImpl(TeacherRepository teacherRepository,
                              ActivityLogService activityLogService) {
        this.teacherRepository = teacherRepository;
        this.activityLogService = activityLogService;
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

        // The form posts both new and edited teachers here
        boolean isNew = teacher.getId() == null;
        Teacher saved = teacherRepository.save(teacher);

        activityLogService.record(isNew ? ActivityLog.CREATED : ActivityLog.UPDATED,
                "Teacher", fullName(saved));
        return saved;
    }

    @Override
    public Teacher updateTeacher(Teacher teacher) {

        Teacher saved = teacherRepository.save(teacher);

        activityLogService.record(ActivityLog.UPDATED, "Teacher", fullName(saved));
        return saved;
    }

    @Override
    public void deleteTeacher(Long id) {

        // Read the name first: afterwards there is nothing left to describe.
        // An unknown id deletes nothing, so it is not logged either.
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher == null) {
            return;
        }
        String name = fullName(teacher);

        teacherRepository.deleteById(id);

        activityLogService.record(ActivityLog.DELETED, "Teacher", name);
    }

    private static String fullName(Teacher teacher) {
        return teacher.getFirstName() + " " + teacher.getLastName();
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