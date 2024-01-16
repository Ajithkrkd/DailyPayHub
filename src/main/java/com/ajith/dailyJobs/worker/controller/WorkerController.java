package com.ajith.dailyJobs.worker.controller;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.WorkerNotFoundException;
import com.ajith.dailyJobs.worker.Requests.WorkerDetailsUpdateRequest;
import com.ajith.dailyJobs.worker.Response.WorkerDetailsResponse;
import com.ajith.dailyJobs.worker.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;
    @GetMapping ("/details")
    public ResponseEntity <?> getUserDetails(@RequestHeader ("Authorization") String token) {
        try {

            WorkerDetailsResponse userDetails = workerService.getUserDetails ( token.substring ( 7 ) );
            return ResponseEntity.ok ( userDetails );
        } catch (WorkerNotFoundException e) {
            return ResponseEntity.status ( HttpStatus.UNAUTHORIZED ).body ( e.getMessage ( ) );
        }
    }


    @PostMapping ("/update-worker")
    public ResponseEntity<?> updateUserDetails(
            @RequestHeader ("Authorization") String token,
            @RequestBody WorkerDetailsUpdateRequest workerDetailsUpdateRequest
    ){
        try {
            workerService.updateUserDetails(token, workerDetailsUpdateRequest );
            return ResponseEntity.ok("Worker details updated successfully");
        } catch (WorkerNotFoundException e) {
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/addProfilePic")
    public ResponseEntity<?> addProfilePic(
            @RequestHeader("Authorization") String token,
            @RequestParam ("file") MultipartFile file
    ) throws WorkerNotFoundException {
        try {
            System.out.println (file);
            System.out.println (token);
            String fileName = workerService.updateProfilePicture(token, file);
            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (WorkerNotFoundException e) {
            throw new  WorkerNotFoundException(e.getMessage ());
        }
    }
}
