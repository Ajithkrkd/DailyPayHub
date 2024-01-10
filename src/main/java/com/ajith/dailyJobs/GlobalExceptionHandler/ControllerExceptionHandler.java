package com.ajith.dailyJobs.GlobalExceptionHandler;

import com.ajith.dailyJobs.auth.Exceptions.UserBlockedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler (value = {UserBlockedException.class})
    @ResponseStatus (value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(UserBlockedException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.NOT_FOUND.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "user is blocked try to connect with support" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
}