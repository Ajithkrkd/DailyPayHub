package com.ajith.dailyJobs.user.Response;
import lombok.*;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDetailsResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Optional <String> imageUrl;
}