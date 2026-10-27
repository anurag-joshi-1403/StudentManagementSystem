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

    // The column sizes, as in V1__baseline.sql
    public static final int DESCRIPTION_LENGTH = 255;
    public static final int USERNAME_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Created, Updated, Deleted or Imported
    @Column(nullable = false, length = 20)
    private String action;

    // Student, Teacher, Fee or Result
    @Column(name = "entity_type", nullable = false, length = 30)
    private String entityType;

    @Column(nullable = false, length = DESCRIPTION_LENGTH)
    private String description;

    @Column(nullable = false, length = USERNAME_LENGTH)
    private String username;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ActivityLog() {
    }

    public ActivityLog(String action, String entityType, String description, String username) {
        this.action = action;
        this.entityType = entityType;
        // Cut to fit (#40). A line is written after the change it records,
        // outside that change's transaction, so a line too long for its
        // column used to fail with the change already saved, and the user
        // was told that nothing had changed.
        this.description = fit(description, DESCRIPTION_LENGTH);
        this.username = fit(username, USERNAME_LENGTH);
        this.createdAt = LocalDateTime.now();
    }

    // The start of the text, ending in "…" when it had to be cut
    private static String fit(String text, int max) {
        if (text == null || text.length() <= max) {
            return text;
        }
        int end = max - 1;
        // Never split a character made of two chars, such as an emoji
        if (Character.isHighSurrogate(text.charAt(end - 1))) {
            end--;
        }
        return text.substring(0, end) + "…";
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
