package com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions;

public class WorkerNotFoundException extends Throwable {
    public WorkerNotFoundException (String message) {
        super(message);
    }
}
