package com.ajith.dailyJobs.verificationDocsOfCompany.entity;
import com.ajith.dailyJobs.admin.verificationDocsType.entity.VerificationDocType;
import com.ajith.dailyJobs.company.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VerificationDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer verificationDocId;


    private VerificationDocType verificationDocType;

    private String verificationDocImageURL;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
