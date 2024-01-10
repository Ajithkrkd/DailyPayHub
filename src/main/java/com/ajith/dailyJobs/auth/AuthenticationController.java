package com.ajith.dailyJobs.auth;
import com.ajith.dailyJobs.auth.Exceptions.UserBlockedException;
import com.ajith.dailyJobs.auth.Requests.AuthenticationRequest;
import com.ajith.dailyJobs.auth.Requests.RegisterRequest;
import com.ajith.dailyJobs.auth.Response.AuthenticationResponse;
import com.ajith.dailyJobs.user.Exceptions.CustomAuthenticationException;
import com.ajith.dailyJobs.user.Requests.UserDetailsUpdateRequest;
import com.ajith.dailyJobs.user.Response.UserDetailsResponse;
import com.ajith.dailyJobs.user.repository.UserRepository;
import com.ajith.dailyJobs.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping ("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;
    private  final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity< AuthenticationResponse > register(@RequestBody RegisterRequest request) {
        try {
            boolean existEmail = userService.isEmailExist(request.getEmail());
            if (existEmail) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthenticationResponse.builder()
                .error("Email already exists")
                .build());
            }

            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthenticationResponse.builder()
                            .error("An error occurred during registration")
                            .build());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity < ? > register(
            @RequestBody AuthenticationRequest request
    ){
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
            return ResponseEntity.status(403).body("Invalid email or password");
        }  catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
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
    @GetMapping ("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader ("Authorization") String token) {
        try {

            UserDetailsResponse userDetails = userService.getUserDetails ( token.substring ( 7 ) );
            return ResponseEntity.ok ( userDetails );
        } catch (CustomAuthenticationException e) {
            return ResponseEntity.status ( HttpStatus.UNAUTHORIZED ).body ( e.getMessage ( ) );
        }
    }


    @PostMapping ("/update-user")
    public ResponseEntity<?> updateUserDetails(
            @RequestHeader ("Authorization") String token,
            @RequestBody UserDetailsUpdateRequest userDetailsUpdateRequest
    ){
        try {
            userService.updateUserDetails(token, userDetailsUpdateRequest);
            return ResponseEntity.ok("User details updated successfully");
        } catch (CustomAuthenticationException e) {
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }




}