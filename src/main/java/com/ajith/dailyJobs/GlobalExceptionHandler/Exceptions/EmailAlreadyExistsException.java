package com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException (String message) {
        super(message);
    }
}
