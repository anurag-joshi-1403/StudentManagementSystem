package com.anurag.sms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * One line of the audit trail behind the dashboard's Recent Activity panel
 * (F3): who created, updated or deleted which record, and when.
 *
 * description is plain text written at the time ("Diya Patel"), not a
 * foreign key, so the line still reads correctly after the record it
 * describes has been deleted.
 */
@Entity
@Table(name = "activity_log", indexes = @Index(name = "idx_activity_created_at", columnList = "created_at"))
public class ActivityLog {

    public static final String CREATED = "Created";
    public static final String UPDATED = "Updated";
    public static final String DELETED = "Deleted";
    public static final String IMPORTED = "Imported";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Created, Updated, Deleted or Imported
    @Column(nullable = false, length = 20)
    private String action;

    // Student, Teacher, Fee or Result
    @Column(name = "entity_type", nullable = false, length = 30)
    private String entityType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ActivityLog() {
    }

    public ActivityLog(String action, String entityType, String description, String username) {
        this.action = action;
        this.entityType = entityType;
        this.description = description;
        this.username = username;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
