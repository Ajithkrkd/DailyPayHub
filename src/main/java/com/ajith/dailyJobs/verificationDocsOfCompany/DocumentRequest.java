package com.ajith.dailyJobs.verificationDocsOfCompany;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DocumentRequest {
    private String documentType;
    private MultipartFile file;
}
