package com.anurag.sms.exception;

/**
 * Exception thrown when a student
 * cannot be found in the database.
 *
 * @author Anurag Joshi
 */
public class StudentNotFoundException extends Exception {

    /**
     * Creates a StudentNotFoundException
     * with the specified message.
     *
     * @param message Exception message
     */
    public StudentNotFoundException(String message) {
        super(message);
    }
}
