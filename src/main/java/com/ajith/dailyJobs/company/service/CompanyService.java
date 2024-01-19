package com.ajith.dailyJobs.company.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.InternalServerException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.WorkerNotFoundException;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.company.CompanyRegisterRequest;
import com.ajith.dailyJobs.company.Response.CompanyResponse;
import org.springframework.http.ResponseEntity;

public interface CompanyService {
    ResponseEntity< BasicResponse>
    registerCompany (
            CompanyRegisterRequest companyRegisterRequest
            ,Long workerId
    ) throws WorkerNotFoundException;

    void setTokenForVerification (String token, String email);

    ResponseEntity<BasicResponse> confirmEmailWithToken (String token);

    ResponseEntity< CompanyResponse> getCompanyDetailsByWorkerId (Long workerId) throws WorkerNotFoundException, CompanyNotFountException, InternalServerException;
}
