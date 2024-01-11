package com.ajith.dailyJobs.common;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicResponse {
    private int status;
    private LocalDateTime timestamp;
    private String message;
    private String description;

}