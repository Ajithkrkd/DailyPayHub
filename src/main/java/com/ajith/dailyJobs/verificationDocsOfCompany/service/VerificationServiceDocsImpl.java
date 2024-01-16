package com.ajith.dailyJobs.verificationDocsOfCompany.service;

import com.ajith.dailyJobs.verificationDocsOfCompany.enums.VerificationDocsType;
import com.ajith.dailyJobs.verificationDocsOfCompany.repository.VerificationDocsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationServiceDocsImpl implements VerificationDocsService{
    private final VerificationDocsRepository verificationDocsRepository;


}
