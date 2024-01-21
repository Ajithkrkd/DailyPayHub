package com.ajith.dailyJobs.company.controller;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.InternalServerException;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.company.Requests.AddressRequest;
import com.ajith.dailyJobs.company.Response.CompanyAddressResponse;
import com.ajith.dailyJobs.company.service.CompanyAddressService;
import com.ajith.dailyJobs.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/worker/company/address")
@RequiredArgsConstructor
public class CompanyAddressController {

    private final CompanyAddressService companyAddressService;
    @PostMapping("/create/{companyId}")
    public ResponseEntity<BasicResponse> createCompanyAddress(
                            @PathVariable("companyId") String companyId,
                            @RequestBody AddressRequest addressRequest) throws InternalServerException, CompanyNotFountException {
        try{
            companyAddressService.createAddress(addressRequest, Integer.valueOf ( companyId ) );
            return ResponseEntity.status ( HttpStatus.CREATED ).body (
                    BasicResponse
                            .builder ()
                            .status ( HttpStatus.CREATED.value () )
                            .timestamp ( LocalDateTime.now ())
                            .description ( "Company Address CREATED" )
                            .message ( "Company Address created successfully" )
                    .build () );
        }
        catch (CompanyNotFountException e) {
            throw new CompanyNotFountException ( e.getMessage () );
        }
        catch (Exception e) {
            e.printStackTrace ();
            throw new InternalServerException ( e.getMessage () );
        }
    }

    @GetMapping("/getCompanyAddress/{companyId}")
    public ResponseEntity< CompanyAddressResponse > getCompanyAddress(@PathVariable String companyId) throws CompanyNotFountException,InternalServerException {
     try{
        return companyAddressService.getCompanyAddress( Integer.valueOf ( companyId ) );
     }
     catch (CompanyNotFountException e) {
         throw new CompanyNotFountException ( e.getMessage () );
     }
     catch (Exception e) {
         e.printStackTrace ();
         throw new InternalServerException (e.getMessage ());
     }
    }
}
