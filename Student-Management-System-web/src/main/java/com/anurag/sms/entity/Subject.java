package com.anurag.sms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject Code is required")
    private String subjectCode;

    @NotBlank(message = "Subject Name is required")
    private String subjectName;

    @NotBlank(message = "Semester is required")
    private String semester;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    private Integer credits;

    @NotBlank(message = "Department is required")
    private String department;

    public Subject() {
    }

    public Subject(Long id,
            String subjectCode,
            String subjectName,
            String semester,
            Integer credits,
            String department) {
        this.id = id;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.semester = semester;
        this.credits = credits;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}