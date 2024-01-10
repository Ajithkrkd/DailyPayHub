package com.ajith.dailyJobs.user.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsUpdateRequest {
    private String firtName;
    private String lastName;
    private String email;
    private Optional <String> password;
}
