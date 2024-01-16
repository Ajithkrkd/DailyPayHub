package com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions;

public class CompanyNameAlreadyExistsException extends RuntimeException {
    public CompanyNameAlreadyExistsException (String message) {
    super(message);
    }
}
