package com.ajith.dailyJobs.worker.Response;
import lombok.*;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WorkerDetailsResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String joinDate;
    private Optional <String> imageUrl;
}