package com.anurag.sms.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * App-wide handlers for exceptions that would otherwise reach the user as
 * a Whitelabel 500 page (#8).
 *
 * Not-found lookups need no handler here: ResourceNotFoundException is
 * annotated with @ResponseStatus(NOT_FOUND), so Spring renders error/404.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * A database constraint rejected the change: a delete that other rows
     * still point at, or a duplicate value in a unique column. Every known
     * delete path clears its children first, so this is a safety net.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        log.warn("Constraint violation: {}", ex.getMostSpecificCause().getMessage());

        return "error/409";
    }
}
