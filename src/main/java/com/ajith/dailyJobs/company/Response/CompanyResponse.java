package com.ajith.dailyJobs.company.Response;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompanyResponse {
    private Integer companyId;
    private String companyName;
    private String companyOwnerName;
    private String companyEmail;
    private boolean isCompanyEmailVerified;
    private boolean isCompanyDocumentVerified;
    private String companyLogoUrl;
    private String companyNumber;
    private Integer companyAddressId;
}
