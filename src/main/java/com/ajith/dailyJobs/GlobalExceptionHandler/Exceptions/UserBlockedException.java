package com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException(String message) {
        super(message);
    }
}