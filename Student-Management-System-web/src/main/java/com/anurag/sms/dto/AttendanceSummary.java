package com.anurag.sms.dto;

/**
 * One student's attendance: how many records say "Present" out of all their
 * records. Used by the student profile (E11) and the attendance report
 * (E12), so the two always agree. Late and Absent both count as not present.
 *
 * Boxed Longs because the report fills it with a JPQL "SELECT new" query,
 * whose COUNT and SUM results are Long.
 */
public record AttendanceSummary(
        Long studentId,
        String firstName,
        String lastName,
        Long present,
        Long total) {

    /** Below this percentage a student is flagged on the report. */
    public static final double MINIMUM_PERCENT = 75;

    public String studentName() {
        return firstName + " " + lastName;
    }

    public boolean hasRecords() {
        return total > 0;
    }

    public double percentage() {
        return hasRecords() ? present * 100.0 / total : 0;
    }

    /** False for a student with no records yet: there is nothing to flag. */
    public boolean isBelowMinimum() {
        return hasRecords() && percentage() < MINIMUM_PERCENT;
    }
}
