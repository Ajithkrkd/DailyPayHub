package com.ajith.dailyJobs.verificationDocsOfCompany.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.EmailNotVerifiedException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.InternalServerException;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.company.entity.Company;
import com.ajith.dailyJobs.company.repository.CompanyRepository;
import com.ajith.dailyJobs.verificationDocsOfCompany.DocumentRequest;
import com.ajith.dailyJobs.verificationDocsOfCompany.entity.VerificationDocs;
import com.ajith.dailyJobs.verificationDocsOfCompany.repository.VerificationDocsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationServiceDocsImpl implements VerificationDocsService{
    private final VerificationDocsRepository verificationDocsRepository;

    private  final CompanyRepository companyRepository;
    @Override
    public ResponseEntity < BasicResponse > saveDocuments (
                            List < DocumentRequest > documentRequestList ,Integer companyId) throws IOException, CompanyNotFountException, InternalServerException {
            try {
                Optional < Company > companyOptional = companyRepository.findByCompanyId(companyId);
                if(companyOptional.isPresent()) {
                    Company existCompany = companyOptional.get ( );
                    boolean isVerified = existCompany.isCompanyEmailVerified ();
                    if(!isVerified) {
                        throw new EmailNotVerifiedException ( "Your email  "+existCompany.getCompanyEmail ()+"   is not verified" );
                    }

                    for (int i = 0; i < documentRequestList.size ( ); i++) {
                        System.out.println ( documentRequestList.get ( i ).getDocumentType ( ) + "----------list" + i );
                        System.out.println ( documentRequestList.get ( i ).getFile ( ).getOriginalFilename ( ) + "----------list" + i );
                        String fileName = uploadImageToLocalDir (
                                documentRequestList.get ( i ).getFile ( ),
                                documentRequestList.get ( i ).getDocumentType ( ),
                                companyId
                        );
                        VerificationDocs verificationDocs = new VerificationDocs ( );
//                      verificationDocs.setVerificationDocType (documentRequestList.get ( i ).getDocumentType ());
                        verificationDocs.setVerificationDocImageURL ( "/uploads/verificationDoc/" + companyId + "/" + documentRequestList.get ( i ).getDocumentType ( ) + "/" + fileName );
                        verificationDocs.setCompany ( existCompany );
                        verificationDocsRepository.save ( verificationDocs);
                    }
                    return ResponseEntity.status ( HttpStatus.CREATED ).body (
                            BasicResponse.builder ()
                                    .status ( HttpStatus.CREATED.value ( ) )
                                    .message ( "Verification documents uploaded" )
                                    .timestamp ( LocalDateTime.now () )
                                    .description ( "Verification document uploaded successfully" )
                                    .build () );
                }
                else{
                    throw new CompanyNotFountException ("company not found with id " +companyId);
                }

            }

            catch ( CompanyNotFountException  e)
            {
                throw new CompanyNotFountException ("company not found with id " +companyId);
            }
            catch (EmailNotVerifiedException e){
                throw  new EmailNotVerifiedException ( e.getMessage () );
            }
            catch (Exception e) {
                e.printStackTrace ();
                throw new InternalServerException ( "server error" );
            }

    }

    private String uploadImageToLocalDir (MultipartFile imageFile, String documentType ,Integer companyId) throws IOException {
           String rootPath = System.getProperty("user .dir");
            String uploadDir = rootPath + "/src/main/resources/static/uploads/verificationDoc/"+companyId+"/"+documentType;
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = imageFile.getOriginalFilename();
            String filePath = uploadDir + "/" + fileName;
            Path path = Paths.get(filePath);

            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException e) {
                // Handle the file copy exception
                throw new IOException("Failed to copy profile picture file", e);
            }

    }
}
