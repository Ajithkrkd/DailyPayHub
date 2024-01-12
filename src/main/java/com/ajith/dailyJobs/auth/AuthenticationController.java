package com.ajith.dailyJobs.auth;

import com.ajith.dailyJobs.auth.Exceptions.UserBlockedException;
import com.ajith.dailyJobs.auth.Requests.AuthenticationRequest;
import com.ajith.dailyJobs.auth.Requests.RegisterRequest;
import com.ajith.dailyJobs.auth.Response.AuthenticationResponse;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping ("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity< BasicResponse > register(@RequestBody RegisterRequest request) {
        try {
            boolean existEmail = userService.isEmailExist(request.getEmail());
            if (existEmail) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BasicResponse.builder()
                .message ("Email already exists")
                        .timestamp ( LocalDateTime.now () )
                        .description ( "There is conflict with already existing email" )
                        .status ( HttpStatus.CONFLICT.value ( ) )
                .build());
            }

            ResponseEntity<BasicResponse> response = service.register(request);
            return response;
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BasicResponse.builder()
                            .message ("An error occurred during registration")
                            .description ( "server side error" )
                            .timestamp ( LocalDateTime.now () )
                            .status ( HttpStatus.INTERNAL_SERVER_ERROR.value ( ) )
                            .build());
        }
    }

    @PostMapping("/verify-email/{email}")
    public ResponseEntity<BasicResponse> verifyUserEmail(@PathVariable String email)
            throws MessagingException, UnsupportedEncodingException {
        try {
            System.out.println (email  + "--------from here");
            String token = UUID.randomUUID ().toString ();

            userService.setTokenForVerification(token,email);

            String verificationLink = "http://localhost:5173" + "/login?token=" + token;
            service.sentMailForVerification (email, verificationLink );
            return ResponseEntity.status ( HttpStatus.OK )
                    .body ( BasicResponse.builder ()
                            .message ("Email verification sent successfully")
                            .description ( "Verification sent successfully to user email with token click to verify" )
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
       return userService.cofirmEmailwithToken(token,email);
    }

    @PostMapping("/authenticate/{verified}")
    public ResponseEntity < ? > register(@PathVariable Boolean verified,
            @RequestBody AuthenticationRequest request
    ){

        if(!verified)
        {
            return ResponseEntity.status ( HttpStatus.UNAUTHORIZED )
                    .body ( AuthenticationResponse.builder ()
                            .message ( "authentication failed User not verified his Email" )
                            .accessToken ( null )
                            .refreshToken ( null )
                            .build ());
        }
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.status(403).body("User not found");
        }
        catch (UserBlockedException e) {
            throw new UserBlockedException ( e.getMessage() );
        }

        catch (BadCredentialsException e) {
            return ResponseEntity.status(403).body(
                    AuthenticationResponse.builder ()
                            .message ( "Invalid Email or Password" )
                            .build ()
            );
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    AuthenticationResponse.builder ()
                            .message ( "Server Not Responding " )
                            .build ()
            );
        }
    }

    @PostMapping("/refresh-token")
    private AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        System.out.println ("Refreshing token" );
        return  service.refreshToken(request, response);
    }




}