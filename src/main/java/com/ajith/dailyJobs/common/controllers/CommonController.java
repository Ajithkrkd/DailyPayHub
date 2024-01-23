package com.ajith.dailyJobs.common.controllers;

import com.ajith.dailyJobs.admin.verificationDocsType.entity.VerificationDocType;
import com.ajith.dailyJobs.admin.verificationDocsType.service.VerificationDocsTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonController {

    private  final VerificationDocsTypeService verificationDocsTypeService;
    @GetMapping ("/get-all-verification-docs")
    public ResponseEntity < List < VerificationDocType > > getNonDeletedVerificationDocs()
    {
        return verificationDocsTypeService.getAllVerificationDocTypes();
    }
}
