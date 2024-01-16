package com.ajith.dailyJobs.company.controller;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.WorkerNotFoundException;
import com.ajith.dailyJobs.auth.AuthenticationService;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.company.CompanyRegisterRequest;
import com.ajith.dailyJobs.company.service.CompanyService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/worker/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private  final AuthenticationService service;
    @PostMapping("/register/{workerId}")
    public ResponseEntity< BasicResponse > registerCompany(

            @RequestBody CompanyRegisterRequest companyRegisterRequest, @PathVariable("workerId") String workerId
           ) throws WorkerNotFoundException {
        return companyService.registerCompany (companyRegisterRequest , Long.valueOf ( workerId ) );
    }

    @PostMapping("/verify-email/{email}")
    public ResponseEntity<BasicResponse> verifyUserEmail(
            @PathVariable String email)
            throws MessagingException, UnsupportedEncodingException {
        try {
            String token = UUID.randomUUID ().toString ();
            companyService.setTokenForVerification(token,email);

            String verificationLink = "http://localhost:5173" + "/userProfile?token=" + token;
            service.sentMailForVerification (email, verificationLink );
            return ResponseEntity.status ( HttpStatus.OK )
                    .body ( BasicResponse.builder ()
                            .message ("Email verification sent successfully")
                            .description ( "Verification sent successfully to company email with token click to verify" )
                            .timestamp ( LocalDateTime.now () )
                            .status ( HttpStatus.OK.value ( ) )
                            .build ());


        }
        catch (Exception e)
        {
            e.printStackTrace ();
            return ResponseEntity.status ( HttpStatus.INTERNAL_SERVER_ERROR)
                    .body ( BasicResponse.builder ()
                            .message ("An Error Occurred")
                            .description ( "Verification Failed because of some Server Error " )
                            .timestamp ( LocalDateTime.now () )
                            .status ( HttpStatus.INTERNAL_SERVER_ERROR.value () )

                            .build () );
        }
    }
    @PostMapping("/confirm-email/{token}/{email}")
    public ResponseEntity<BasicResponse>confirmUserEmail(
            @PathVariable String token,
            @PathVariable String email)
    {
        return companyService.confirmEmailWithToken (token,email);
    }

}
