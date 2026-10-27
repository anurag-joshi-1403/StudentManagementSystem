package com.anurag.sms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.anurag.sms.utility.GradeCalculator;

@Entity
// One result per student and exam (#41): a second one made the marksheet
// count the exam twice. The form checks first; the database index backs it
// up. Created by V7__results_unique.sql.
@Table(name = "results", uniqueConstraints = @UniqueConstraint(
        name = "uk_results_student_exam",
        columnNames = {"student_id", "exam_id"}))
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    @NotNull(message = "Exam is required")
    private Exam exam;

    @NotNull(message = "Obtained marks are required")
    @Min(value = 0, message = "Marks cannot be negative")
    @Max(value = GradeCalculator.MAX_MARKS, message = "Invalid marks")
    private Integer obtainedMarks;

    private String grade;

    private String resultStatus;

    public Result() {
    }

    public Result(Long id, Student student, Exam exam,
            Integer obtainedMarks,
            String grade,
            String resultStatus) {
        this.id = id;
        this.student = student;
        this.exam = exam;
        this.obtainedMarks = obtainedMarks;
        this.grade = grade;
        this.resultStatus = resultStatus;
    }

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

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Integer getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(Integer obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
}