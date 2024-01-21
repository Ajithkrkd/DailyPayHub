package com.ajith.dailyJobs.admin.verificationDocsType.service;

import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocsTypeRequest;
import com.ajith.dailyJobs.admin.verificationDocsType.entity.VerificationDocType;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.verificationDocsOfCompany.repository.VerificationDocsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class VerificationDocsTypeServiceImpl implements  VerificationDocsTypeService{

    private final VerificationDocsRepository verificationDocsRepository;
    /**
     * @param request
     * @return
     */
    @Override
    public ResponseEntity < BasicResponse > createVerificationDocsType (VerificationDocsTypeRequest request) {
        try{
          var doc =  VerificationDocType.builder ()
                            .documentTypeName ( request.getDocumentTypeName ( ) )
                                    .description ( request.getDescription ( ) )
                                            .createdAt ( LocalDateTime.now () )
                                                    .isDeleted ( false )
                                                            .build ();
            verificationDocsRepository.save ( doc );
          return ResponseEntity.status ( HttpStatus.CREATED ).body ( BasicResponse.builder ()
                           .timestamp ( LocalDateTime.now () )
                           .status ( HttpStatus.CREATED.value () )
                           .description ( "new verification doc type created" )
                           .message ( "verification doc created successfully" )
                   .build () );
        }
        catch ( Exception e )
        {
            throw new RuntimeException ( e.getMessage () );
        }
    }
}
