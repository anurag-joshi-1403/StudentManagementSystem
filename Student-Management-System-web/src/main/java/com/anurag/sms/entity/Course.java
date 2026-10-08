package com.anurag.sms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
// Course codes are unique in the database too, not only in the controller's
// check (#27). Declared on the table, not as @Column(unique = true): Hibernate's
// ddl-auto=update adds table constraints to an existing table, column ones only
// when it creates the table.
@Table(name = "Courses", uniqueConstraints = @UniqueConstraint(columnNames = "course_code"))
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course Code is required")
    private String courseCode;

    @NotBlank(message = "Course Name is required")
    private String courseName;

    @NotBlank(message = "Duration is required")
    private String duration;

    @NotNull(message = "Fees are required")
    @Min(value = 0, message = "Fees cannot be negative")
    private Double fees;

    private String description;

    public Course() {
    }

    public Course(Long id,
                  String courseCode,
                  String courseName,
                  String duration,
                  Double fees,
                  String description) {

        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.duration = duration;
        this.fees = fees;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}