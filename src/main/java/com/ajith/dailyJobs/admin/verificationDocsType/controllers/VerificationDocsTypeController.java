package com.ajith.dailyJobs.admin.verificationDocsType.controllers;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.InternalServerException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.VerificationDocNotFoundException;
import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocTypeUpdateRequest;
import com.ajith.dailyJobs.admin.verificationDocsType.Request.VerificationDocsTypeRequest;
import com.ajith.dailyJobs.admin.verificationDocsType.entity.VerificationDocType;
import com.ajith.dailyJobs.admin.verificationDocsType.service.VerificationDocsTypeService;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.verificationDocsOfCompany.service.VerificationDocsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/verification-doc-type")
@RequiredArgsConstructor
public class VerificationDocsTypeController {

    private final VerificationDocsTypeService verificationDocsTypeService;

    @PostMapping("/create")
    public ResponseEntity< BasicResponse > createVerificationDocsType(
           @RequestBody VerificationDocsTypeRequest request)
    {
        return  verificationDocsTypeService.createVerificationDocsType(request);
    }

    @GetMapping("/getAll")
    public ResponseEntity< List < VerificationDocType > > getVerificationDocs()
    {
        return verificationDocsTypeService.getAllVerificationDocTypes();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BasicResponse> updateVerificationDocType(
            @PathVariable String id,
            @RequestBody VerificationDocTypeUpdateRequest updateRequest) throws InternalServerException {

         return verificationDocsTypeService.updateVerificationDocType( Integer.valueOf ( id ), updateRequest);

    }
    @GetMapping("/change-status/{id}")
    public ResponseEntity<BasicResponse> toggleVerificationDocTypeStatus(@PathVariable String id) throws VerificationDocNotFoundException {
        return verificationDocsTypeService.toggleVerificationDocTypeStatus( Integer.valueOf ( id ) );
    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<BasicResponse> delete(@PathVariable String id) throws VerificationDocNotFoundException {
        return verificationDocsTypeService.delete( Integer.valueOf ( id ) );
    }

}
