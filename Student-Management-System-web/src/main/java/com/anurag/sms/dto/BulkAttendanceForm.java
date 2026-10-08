package com.anurag.sms.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * The bulk attendance form (E14): one subject and date, and one entry per
 * student. Bound row by row with th:field="*{entries[i].status}".
 */
public class BulkAttendanceForm {

    @NotNull(message = "Please choose a subject")
    private Long subjectId;

    @NotNull(message = "Please choose a date")
    private LocalDate date;

    @Valid
    private List<Entry> entries = new ArrayList<>();

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    /** One student's row. */
    public static class Entry {

        private Long studentId;

        // Shown on the form, and posted back in a hidden field so the form
        // can be shown again after a validation error
        private String studentName;

        // Null for a student who already has a record for this subject and
        // date: the form shows no options for them and the save skips them
        @Pattern(regexp = "Present|Absent|Late", message = "Choose Present, Absent or Late")
        private String status;

        private String remarks;

        // The existing record's status, for display only. The save checks
        // the database again rather than trusting this.
        private String recordedStatus;

        public Entry() {
        }

        public Entry(Long studentId, String studentName, String recordedStatus) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.recordedStatus = recordedStatus;
            this.status = recordedStatus == null ? "Present" : null;
        }

        public boolean isAlreadyRecorded() {
            return recordedStatus != null;
        }

        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
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

        public String getRecordedStatus() {
            return recordedStatus;
        }

        public void setRecordedStatus(String recordedStatus) {
            this.recordedStatus = recordedStatus;
        }
    }
}
