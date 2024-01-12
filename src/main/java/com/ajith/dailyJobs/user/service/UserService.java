package com.ajith.dailyJobs.user.service;

import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.user.Requests.UserDetailsUpdateRequest;
import com.ajith.dailyJobs.user.Response.UserDetailsResponse;
import com.ajith.dailyJobs.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface UserService{
    UserDetailsResponse getUserDetails (String token);

    void updateUserDetails (String token, UserDetailsUpdateRequest userDetailsUpdateRequest);

    ResponseEntity < String > blockUser (Long userId);

    boolean isEmailExist (String email);

    Optional < User > findUserByName (String userName);

    String updateProfilePicture (String token, MultipartFile file);

    void setTokenForVerification (String token, String email);

    ResponseEntity< BasicResponse > cofirmEmailwithToken (String token, String email);
}