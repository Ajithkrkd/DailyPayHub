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
    private String  companyLogoUrl = null;
    private String companyName;
    private String companyEmail;
    private String companyNumber;
    private String companyOwnerName;
    private boolean isCompanyEmailVerified = false;
    private String emailToken;
    private boolean isCompanyDocumentVerified = false;

    @OneToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_address_id", referencedColumnName = "addressId")
    private CompanyAddress companyAddress;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set < VerificationDocs > verificationDocs;
}
