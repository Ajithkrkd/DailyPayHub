package com.ajith.dailyJobs.admin.verificationDocsType.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.InternalServerException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.VerificationDocNotFoundException;
import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocTypeUpdateRequest;
import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocsTypeRequest;
import com.ajith.dailyJobs.admin.verificationDocsType.entity.VerificationDocType;
import com.ajith.dailyJobs.admin.verificationDocsType.repository.VerificationDocsTypeRepository;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.verificationDocsOfCompany.repository.VerificationDocsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationDocsTypeServiceImpl implements  VerificationDocsTypeService{

    private final VerificationDocsTypeRepository verificationDocsTypeRepository;
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
            verificationDocsTypeRepository.save ( doc );
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

    /**
     * @return
     */
    @Override
    public ResponseEntity < List < VerificationDocType > > getAllVerificationDocTypes ( ) {
       try{
           List<VerificationDocType> verificationDocTypes = verificationDocsTypeRepository.findAll ();

           if (verificationDocTypes.isEmpty()) {
               return ResponseEntity.noContent().build();
           } else {
               return ResponseEntity.ok(verificationDocTypes);
           }
       }catch ( Exception e )
       {
           throw new RuntimeException ( e.getMessage () );
       }

    }

    /**
     * @param id
     * @param updateRequest
     * @return
     */
    @Override
    public ResponseEntity < BasicResponse > updateVerificationDocType (Integer id, VerificationDocTypeUpdateRequest updateRequest) throws InternalServerException {
       try {
           Optional <VerificationDocType> verificationDocTypeOptional = verificationDocsTypeRepository.findById ( id );
           if (verificationDocTypeOptional.isPresent()) {
               VerificationDocType existingVerificationDocType = verificationDocTypeOptional.get ();
               existingVerificationDocType.setDescription ( updateRequest.getDescription() );
               existingVerificationDocType.setDocumentTypeName ( updateRequest.getDocumentTypeName() );
               verificationDocsTypeRepository.save ( existingVerificationDocType );
              return ResponseEntity.status ( HttpStatus.OK ).body ( BasicResponse.builder ()
                       .status (HttpStatus.OK.value ( )  )
                       .description ( "document"+ existingVerificationDocType.getDocumentTypeName ()+ "is updated" )
                       .message ( "verification doc updated successfully" )
                       .timestamp ( LocalDateTime.now () )
                      .build ());
           }else{
               throw new VerificationDocNotFoundException ("verification doc with this id is not available id  = " + id);

           }
       } catch (VerificationDocNotFoundException e) {
           throw new RuntimeException ( e );
       }
       catch (Exception e)
       {
           e.printStackTrace ();
           throw new InternalServerException ( e.getMessage () );
       }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity < BasicResponse > toggleVerificationDocTypeStatus (Integer id) throws VerificationDocNotFoundException {
        try {
            Optional < VerificationDocType > verificationDocTypeOptional = verificationDocsTypeRepository.findById ( id );
            if ( verificationDocTypeOptional.isPresent ( ) ) {
                VerificationDocType doc = verificationDocTypeOptional.get();
                doc.setActive ( !doc.isActive ( ) );
                verificationDocsTypeRepository.save ( doc );
                return ResponseEntity.status ( HttpStatus.OK ).body ( BasicResponse.builder ()
                        .status (HttpStatus.OK.value ( )  )
                        .description (doc.isActive () ? "activated" : "deactivated")
                        .message ( "verification status updated successfully" )
                        .timestamp ( LocalDateTime.now () )
                        .build ());
            }

            else {
            throw new VerificationDocNotFoundException ( "verification doc not fount with this id = "+ id );
            }
        } catch (VerificationDocNotFoundException e) {
            throw new VerificationDocNotFoundException ( e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            throw new RuntimeException ( e.getMessage () );
        }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity < BasicResponse > delete (Integer id) throws VerificationDocNotFoundException {
        try {
            Optional < VerificationDocType > verificationDocTypeOptional = verificationDocsTypeRepository.findById ( id );
            if ( verificationDocTypeOptional.isPresent ( ) ) {
                VerificationDocType doc = verificationDocTypeOptional.get();
                doc.setDeleted ( true );
                verificationDocsTypeRepository.save ( doc );
                return ResponseEntity.status ( HttpStatus.OK ).body ( BasicResponse.builder ()
                        .status (HttpStatus.OK.value ( )  )
                        .description ("document deleted successfully with name = "+ doc.getDocumentTypeName ())
                        .message ( "document deleted successfully" )
                        .timestamp ( LocalDateTime.now () )
                        .build ());
            }

            else {
                throw new VerificationDocNotFoundException ( "verification doc not fount with this id = "+ id );
            }
        } catch (VerificationDocNotFoundException e) {
            throw new VerificationDocNotFoundException ( e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            throw new RuntimeException ( e.getMessage () );
        }
    }
}
