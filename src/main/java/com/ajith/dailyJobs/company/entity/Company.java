package com.ajith.dailyJobs.company.entity;

import com.ajith.dailyJobs.verificationDocsOfCompany.entity.VerificationDocs;
import com.ajith.dailyJobs.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer companyId;
    private String companyName;
    private String companyEmail;
    private String companyNumber;
    private String companyOwnerName;
    private boolean isCompanyDocumentVerified = false;

    @OneToOne
    @JoinColumn(name = "company_id")
    private CompanyAddress address;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Worker worker;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set < VerificationDocs > verificationDocs;
}
