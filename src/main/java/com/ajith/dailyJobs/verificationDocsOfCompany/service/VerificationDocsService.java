package com.ajith.dailyJobs.verificationDocsOfCompany.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.InternalServerException;
import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocsTypeRequest;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.verificationDocsOfCompany.DocumentRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface VerificationDocsService {

    ResponseEntity< BasicResponse > saveDocuments (
            List< DocumentRequest> documentRequestList ,Integer companyId) throws IOException, CompanyNotFountException, InternalServerException;

}
