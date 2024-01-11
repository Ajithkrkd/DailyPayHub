package com.ajith.dailyJobs.user.controller;

import com.ajith.dailyJobs.user.Exceptions.CustomAuthenticationException;
import com.ajith.dailyJobs.user.Requests.UserDetailsUpdateRequest;
import com.ajith.dailyJobs.user.Response.UserDetailsResponse;
import com.ajith.dailyJobs.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping ("/details")
    public ResponseEntity <?> getUserDetails(@RequestHeader ("Authorization") String token) {
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
    @PostMapping("/addProfilePic")
    public ResponseEntity<?> addProfilePic(
            @RequestHeader("Authorization") String token,
            @RequestParam ("file") MultipartFile file
    ) {
        try {
            System.out.println (file);
            System.out.println (token);
            String fileName = userService.updateProfilePicture(token, file);
            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (CustomAuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
