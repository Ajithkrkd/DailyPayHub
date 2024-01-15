package com.ajith.dailyJobs.company;

import jakarta.mail.Address;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegisterRequest {
    private String companyName;
    private String companyEmail;
    private String companyNumber;
    private String companyOwnerName;
    private Address address;
}
