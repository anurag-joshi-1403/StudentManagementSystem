package com.anurag.sms.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.dto.AttendanceSummary;
import com.anurag.sms.dto.BulkAttendanceForm;
import com.anurag.sms.entity.Attendance;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.exception.ResourceNotFoundException;
import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.SubjectRepository;
import com.anurag.sms.service.AttendanceService;
import com.anurag.sms.utility.Pages;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 StudentRepository studentRepository,
                                 SubjectRepository subjectRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", id));
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
    public Page<Attendance> searchAttendance(String keyword, LocalDate attendanceDate, int pageNo) {

        // A blank keyword means "no keyword filter", which the query
        // expresses as null. Passing "" would match every row (#26).
        String trimmed = (keyword == null || keyword.isBlank())
                ? null
                : keyword.trim();

        // Search used to return every match on one page (#11).
        return attendanceRepository.search(trimmed, attendanceDate, Pages.of(pageNo));
    }

    @Override
    public long getTotalAttendance() {
        return attendanceRepository.count();
    }

    @Override
    public long countByStatus(String status) {
        return attendanceRepository.countByStatus(status);
    }

    @Override
    public AttendanceSummary getAttendanceSummary(Student student) {

        long present = attendanceRepository.countByStudentIdAndStatus(student.getId(), "Present");
        long total = attendanceRepository.countByStudentId(student.getId());

        return new AttendanceSummary(student.getId(), student.getFirstName(),
                student.getLastName(), present, total);
    }

    @Override
    public List<AttendanceSummary> getAttendanceReport(Long subjectId) {
        return attendanceRepository.summarise(subjectId);
    }

    @Override
    public List<BulkAttendanceForm.Entry> getBulkEntries(Long subjectId, LocalDate date) {

        // One query for the class's existing records, not one per student
        Map<Long, String> recorded = new HashMap<>();
        for (Attendance a : attendanceRepository.findBySubjectIdAndAttendanceDate(subjectId, date)) {
            recorded.put(a.getStudent().getId(), a.getStatus());
        }

        List<BulkAttendanceForm.Entry> entries = new ArrayList<>();
        for (Student s : studentRepository.findAll(Sort.by("firstName", "lastName"))) {
            entries.add(new BulkAttendanceForm.Entry(s.getId(),
                    s.getFirstName() + " " + s.getLastName(), recorded.get(s.getId())));
        }
        return entries;
    }

    @Override
    @Transactional
    public int saveBulk(BulkAttendanceForm form) {

        Subject subject = subjectRepository.findById(form.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", form.getSubjectId()));

        // Guards against the same student appearing twice in one submission
        Set<Long> seen = new HashSet<>();
        int saved = 0;

        for (BulkAttendanceForm.Entry entry : form.getEntries()) {

            // No status = a row the form showed as already recorded
            if (entry.getStudentId() == null || entry.getStatus() == null
                    || !seen.add(entry.getStudentId())) {
                continue;
            }

            // Checked against the database, not the form, so a second
            // submission of the same class saves nothing (E15)
            if (attendanceRepository.existsByStudentIdAndSubjectIdAndAttendanceDate(
                    entry.getStudentId(), subject.getId(), form.getDate())) {
                continue;
            }

            Student student = studentRepository.findById(entry.getStudentId()).orElse(null);
            if (student == null) {
                continue;
            }

            String remarks = entry.getRemarks() == null || entry.getRemarks().isBlank()
                    ? null
                    : entry.getRemarks().trim();

            attendanceRepository.save(new Attendance(null, student, subject,
                    form.getDate(), entry.getStatus(), remarks));
            saved++;
        }

        return saved;
    }

}