package com.ajith.dailyJobs.verificationDocsOfCompany.entity;
import com.ajith.dailyJobs.company.entity.Company;
import com.ajith.dailyJobs.verificationDocsOfCompany.enums.VerificationDocsType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VerificationDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer verificationDocId;

    @Enumerated(EnumType.STRING)
    private VerificationDocsType verificationDocsType;

    private String verificationDocImageURL;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
