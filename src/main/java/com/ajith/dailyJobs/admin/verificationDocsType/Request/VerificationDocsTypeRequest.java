package com.ajith.dailyJobs.admin.verificationDocsType.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationDocsTypeRequest {
    private String DocumentTypeName;
    private String description;
}
