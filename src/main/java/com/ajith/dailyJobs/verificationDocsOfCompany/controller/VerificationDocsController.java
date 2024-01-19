package com.ajith.dailyJobs.verificationDocsOfCompany.controller;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.verificationDocsOfCompany.DocumentRequest;
import com.ajith.dailyJobs.verificationDocsOfCompany.service.VerificationDocsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/worker/company/verification")
@RequiredArgsConstructor
public class VerificationDocsController {

    private final VerificationDocsService verificationDocsService;
    private static final Logger logger = LoggerFactory.getLogger(VerificationDocsController.class);
    @RequestMapping("/documentUpload/{companyId}")
    public ResponseEntity<?> uploadDocuments(
            @RequestParam("files") List< MultipartFile > files,
            @RequestParam("documentTypes") List<String> documentTypes
            ,@PathVariable Integer companyId) throws CompanyNotFountException {
        try {
            List<DocumentRequest> documentRequestList = new ArrayList <> ();
            for (int i = 0; i < files.size(); i++) {
                DocumentRequest documentRequest = new DocumentRequest();
                documentRequest.setFile(files.get(i));
                documentRequest.setDocumentType(documentTypes.get(i));
                documentRequestList.add(documentRequest);
            }

           return verificationDocsService.saveDocuments(documentRequestList,companyId);
        }
        catch (CompanyNotFountException e) {
            throw new CompanyNotFountException ( e.getMessage () );
        }
        catch (Exception e) {
            // Log any exceptions that occur during processing
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing document requests");
        }
    }
}
