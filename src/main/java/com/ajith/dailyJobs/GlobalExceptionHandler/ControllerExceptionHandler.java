package com.ajith.dailyJobs.GlobalExceptionHandler;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler (value = {UserBlockedException.class})
    @ResponseStatus (value = HttpStatus.NOT_FOUND)
    public ErrorMessage UserBlockedException(UserBlockedException ex ,WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.NOT_FOUND.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "worker is blocked try to connect with support" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }

    @ExceptionHandler (value = {EmailNotVerifiedException.class})
    @ResponseStatus (value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage EmailVerificationException(EmailNotVerifiedException ex ,WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.UNAUTHORIZED.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "worker is Not verified his Email Check mail" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
    @ExceptionHandler(value = {WorkerNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage WorkerNotFoundException(WorkerNotFoundException ex,WebRequest request)
    {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.NOT_FOUND.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "worker is Not Exist" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }

    @ExceptionHandler(value = {EmailAlreadyExistsException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage emailAlreadyExist(EmailAlreadyExistsException ex,WebRequest request)
    {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.CONFLICT.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "There is conflict between emails , this email already in use" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
    @ExceptionHandler(value = {CompanyNameAlreadyExistsException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage CompanyNameAlreadyExistException(CompanyNameAlreadyExistsException ex,WebRequest request)
    {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.CONFLICT.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "This Company Name is Already in use try another name" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
    @ExceptionHandler(value = {CompanyNotFountException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage CompanyNotFountException(CompanyNotFountException ex,WebRequest request)
    {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.NOT_FOUND.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "This Company is Not Valid" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
    @ExceptionHandler(value = {InternalServerException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage InternalServerException(InternalServerException ex,WebRequest request)
    {
        ErrorMessage message = new ErrorMessage();
        message.setStatus (HttpStatus.INTERNAL_SERVER_ERROR.value ());
        message.setMessage ( ex.getMessage() );
        message.setDescription ( "There is server side error" );
        message.setTimestamp ( LocalDateTime.now ( ) );
        return message;
    }
}