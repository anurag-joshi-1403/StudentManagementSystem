package com.anurag.sms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a record looked up by id does not exist.
 *
 * Mapped to 404 so a stale link or a typed-in id shows the "Page not
 * found" page. The bare orElseThrow() calls this replaces surfaced as a
 * 500 instead (#8).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
    }
}
