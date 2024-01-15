package com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions;

public class CustomAuthenticationException extends RuntimeException {

    public CustomAuthenticationException(String message) {
        super(message);
    }
}
