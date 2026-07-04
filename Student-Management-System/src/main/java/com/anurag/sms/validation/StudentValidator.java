package com.anurag.sms.validation;

/**
 * Utility class for validating student details
 * before they are saved or updated.
 *
 * @author Anurag Joshi
 */

public class StudentValidator {
    /**
     * Validates the student's name.
     *
     * @param name Student name
     * @return true if the name is valid, otherwise false
     */
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("[A-Za-z]+( [A-Za-z]+)*");
    }

    /**
     * Validates the student's email address.
     *
     * @param email Student email
     * @return true if the email is valid, otherwise false
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Validates the student's course.
     *
     * @param course Student course
     * @return true if the course is valid, otherwise false
     */
    public static boolean validateCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Validates the student's marks.
     *
     * @param marks Student marks
     * @return true if marks are between 0 and 100,
     *         otherwise false
     */
    public static boolean validateMarks(double marks) {
        return marks >= 0 && marks <= 100;
    }
}
