package com.ajith.dailyJobs.worker.service;

import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.worker.Requests.WorkerDetailsUpdateRequest;
import com.ajith.dailyJobs.worker.Response.WorkerDetailsResponse;
import com.ajith.dailyJobs.worker.entity.Worker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface WorkerService {
    WorkerDetailsResponse getUserDetails (String token);

    void updateUserDetails (String token, WorkerDetailsUpdateRequest workerDetailsUpdateRequest);

    ResponseEntity < BasicResponse > blockUser (Long userId);

    boolean isEmailExist (String email);

    Optional < Worker > findUserByName (String userName);

    String updateProfilePicture (String token, MultipartFile file);

    void setTokenForVerification (String token, String email);

    ResponseEntity< BasicResponse > cofirmEmailwithToken (String token, String email);
}