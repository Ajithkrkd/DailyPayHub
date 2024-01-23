package com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException (String message) {
        super(message);
    }
}
