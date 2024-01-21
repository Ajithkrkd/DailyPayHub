package com.ajith.dailyJobs.admin.verificationDocsType.controllers;

import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocsTypeRequest;
import com.ajith.dailyJobs.admin.verificationDocsType.service.VerificationDocsTypeService;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.verificationDocsOfCompany.service.VerificationDocsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/verificationDocType")
@RequiredArgsConstructor
public class VerificationDocsTypeController {

    private final VerificationDocsTypeService verificationDocsTypeService;

    @PostMapping("/create")
    public ResponseEntity< BasicResponse > createVerificationDocsType(
           @RequestBody VerificationDocsTypeRequest request)
    {
        return  verificationDocsTypeService.createVerificationDocsType(request);;
    }

}
