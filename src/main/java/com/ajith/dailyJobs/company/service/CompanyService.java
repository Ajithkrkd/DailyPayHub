package com.ajith.dailyJobs.company.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.InternalServerException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.WorkerNotFoundException;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.company.CompanyRegisterRequest;
import com.ajith.dailyJobs.company.Requests.AddressRequest;
import com.ajith.dailyJobs.company.Response.CompanyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CompanyService {
    ResponseEntity< BasicResponse>
    registerCompany (
            CompanyRegisterRequest companyRegisterRequest
            ,Long workerId
    ) throws WorkerNotFoundException;

    void setTokenForVerification (String token, String email);

    ResponseEntity<BasicResponse> confirmEmailWithToken (String token);

    ResponseEntity< CompanyResponse> getCompanyDetailsByWorkerId (Long workerId) throws WorkerNotFoundException, CompanyNotFountException, InternalServerException;

    void uploadCompanyLogo (MultipartFile file, Long workerId) throws WorkerNotFoundException, IOException;


}
