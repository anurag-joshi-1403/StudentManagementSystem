package com.anurag.sms.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.anurag.sms.utility.FieldLimits;

@Entity
// One record per student, subject and day (#35). The forms check first; the
// database index backs them up. Created by V2__attendance_unique.sql.
@Table(name = "attendance", uniqueConstraints = @UniqueConstraint(
        name = "uk_attendance_student_subject_date",
        columnNames = {"student_id", "subject_id", "attendance_date"}))
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // Student
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required")
    private Student student;

    // ==========================
    // Subject
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @NotNull(message = "Subject is required")
    private Subject subject;

    // ==========================
    // Attendance Date
    // ==========================

    @NotNull(message = "Attendance date is required")
    private LocalDate attendanceDate;

    // ==========================
    // Status
    // ==========================

    @NotBlank(message = "Status is required")
    private String status;

    // ==========================
    // Remarks
    // ==========================

    @Size(max = FieldLimits.TEXT, message = "Remarks" + FieldLimits.TEXT_TOO_LONG)
    private String remarks;

    // ==========================
    // Constructors
    // ==========================

    public Attendance() {
    }

    public Attendance(Long id, Student student, Subject subject,
            LocalDate attendanceDate,
            String status,
            String remarks) {

        this.id = id;
        this.student = student;
        this.subject = subject;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
    }

    // ==========================
    // Getters and Setters
    // ==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}