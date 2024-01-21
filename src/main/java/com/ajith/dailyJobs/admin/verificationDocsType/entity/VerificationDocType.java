package com.ajith.dailyJobs.admin.verificationDocsType.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VerificationDocType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    private String documentTypeName;

    private String description;

    private boolean isDeleted = false;

    private boolean isActive = true;

    private LocalDateTime createdAt;

}
