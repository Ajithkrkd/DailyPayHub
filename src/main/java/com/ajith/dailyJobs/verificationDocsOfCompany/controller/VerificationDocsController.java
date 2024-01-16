package com.ajith.dailyJobs.verificationDocsOfCompany.controller;

import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.verificationDocsOfCompany.DocumentRequest;
import com.ajith.dailyJobs.verificationDocsOfCompany.service.VerificationDocsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/worker/company/verification")
@RequiredArgsConstructor
public class VerificationDocsController {

    private final VerificationDocsService verificationDocsService;

    @PostMapping("/documentUpload/{companyId}")
    public ResponseEntity< BasicResponse > uploadVerificationDocs(
            @RequestBody List<DocumentRequest> documentRequests,
            @PathVariable ("companyId") Integer companyId){
        System.out.println (companyId );
        for (DocumentRequest request : documentRequests) {
            String documentType = request.getDocumentType();
            MultipartFile file = request.getFile();

            // Process the file and document type as needed
            // In this example, we're just logging them
            System.out.println("Document Type: " + documentType);
            System.out.println("File Name: " + file.getOriginalFilename());
            // You can save the file to the server or perform other operations here
        }
        return null;
    }
}
