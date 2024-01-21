package com.ajith.dailyJobs.admin.verificationDocsType.Request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocTypeUpdateRequest {
    private String documentTypeName;
    private String description;
}
