package com.ajith.dailyJobs.worker.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDetailsUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Optional <String> password;
}
