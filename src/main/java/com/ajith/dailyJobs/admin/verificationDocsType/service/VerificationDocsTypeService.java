package com.ajith.dailyJobs.admin.verificationDocsType.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.InternalServerException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.VerificationDocNotFoundException;
import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocTypeUpdateRequest;
import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocsTypeRequest;
import com.ajith.dailyJobs.admin.verificationDocsType.entity.VerificationDocType;
import com.ajith.dailyJobs.common.BasicResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VerificationDocsTypeService {
    ResponseEntity< BasicResponse > createVerificationDocsType (VerificationDocsTypeRequest request);

    ResponseEntity< List< VerificationDocType>> getAllVerificationDocTypes ( );

    ResponseEntity< BasicResponse> updateVerificationDocType (Integer id, VerificationDocTypeUpdateRequest updateRequest) throws InternalServerException;

    ResponseEntity< BasicResponse> toggleVerificationDocTypeStatus (Integer id) throws VerificationDocNotFoundException;

    ResponseEntity< BasicResponse> delete (Integer id) throws VerificationDocNotFoundException;
}
