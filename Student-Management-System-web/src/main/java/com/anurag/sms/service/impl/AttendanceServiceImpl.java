package com.anurag.sms.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.Attendance;
import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id).orElseThrow();
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance updateAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    @Override
    public List<Attendance> searchAttendance(String keyword, LocalDate attendanceDate) {

        if (attendanceDate != null) {
            return attendanceRepository
                    .findByStudentFirstNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCaseOrAttendanceDate(
                            keyword,
                            keyword,
                            attendanceDate);
        }

        return attendanceRepository
                .findByStudentFirstNameContainingIgnoreCaseOrSubjectSubjectNameContainingIgnoreCaseOrAttendanceDate(
                        keyword,
                        keyword,
                        LocalDate.of(1900, 1, 1));
    }

    @Override
    public Page<Attendance> getAttendanceByPage(int page) {

        return attendanceRepository.findAll(
                PageRequest.of(page - 1, 5));
    }

    @Override
    public long getTotalAttendance() {
        return attendanceRepository.count();
    }

}