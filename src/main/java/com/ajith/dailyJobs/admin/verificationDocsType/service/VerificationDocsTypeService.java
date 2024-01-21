package com.ajith.dailyJobs.admin.verificationDocsType.service;

import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocsTypeRequest;
import com.ajith.dailyJobs.common.BasicResponse;
import org.springframework.http.ResponseEntity;

public interface VerificationDocsTypeService {
    ResponseEntity< BasicResponse > createVerificationDocsType (VerificationDocsTypeRequest request);
}
