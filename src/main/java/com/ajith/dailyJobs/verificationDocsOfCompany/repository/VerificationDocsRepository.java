package com.ajith.dailyJobs.verificationDocsOfCompany.repository;

import com.ajith.dailyJobs.admin.verificationDocsType.entity.VerificationDocType;
import com.ajith.dailyJobs.verificationDocsOfCompany.entity.VerificationDocs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationDocsRepository extends JpaRepository< VerificationDocs, Integer > {
}
