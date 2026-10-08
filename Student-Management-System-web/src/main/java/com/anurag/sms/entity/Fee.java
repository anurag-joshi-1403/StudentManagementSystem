package com.anurag.sms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "fees")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required")
    private Student student;

    @NotBlank(message = "Fee type is required")
    private String feeType;

    @NotNull(message = "Amount is required")
    // Zero is allowed for a waived fee; negatives were accepted before (#28)
    @PositiveOrZero(message = "Amount cannot be negative")
    private BigDecimal amount;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private LocalDate paymentDate;

    @NotBlank(message = "Payment status is required")
    private String paymentStatus;

    private String paymentMethod;

    public Fee() {
    }

    public Fee(Long id, Student student, String feeType,
               BigDecimal amount, LocalDate dueDate,
               LocalDate paymentDate, String paymentStatus,
               String paymentMethod) {
        this.id = id;
        this.student = student;
        this.feeType = feeType;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
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

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Not paid and past its due date (E10). Not a column: the entity uses
    // field access, so JPA ignores this method. FeeRepository's overdue
    // queries apply the same rule in the database.
    public boolean isOverdue() {
        return !"Paid".equals(paymentStatus)
                && dueDate != null
                && dueDate.isBefore(LocalDate.now());
    }
}