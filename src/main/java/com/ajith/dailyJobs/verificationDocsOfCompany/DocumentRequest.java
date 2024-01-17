package com.ajith.dailyJobs.verificationDocsOfCompany;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest  {
    private String documentType;
    private MultipartFile file;
}
