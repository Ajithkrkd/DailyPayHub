package com.ajith.dailyJobs.admin.verificationDocsType.repository;

import com.ajith.dailyJobs.admin.verificationDocsType.entity.VerificationDocType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationDocsTypeRepository extends JpaRepository < VerificationDocType,Integer> {
    Optional< VerificationDocType> findByDocumentTypeName (String documentTypeName);
}
