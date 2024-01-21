package com.ajith.dailyJobs.admin.verificationDocsType.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Service
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

    private LocalDateTime createdAt;

}
