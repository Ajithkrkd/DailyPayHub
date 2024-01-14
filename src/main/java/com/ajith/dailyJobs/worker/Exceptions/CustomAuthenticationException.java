package com.ajith.dailyJobs.worker.Exceptions;

public class CustomAuthenticationException extends RuntimeException {

    public CustomAuthenticationException(String message) {
        super(message);
    }
}
