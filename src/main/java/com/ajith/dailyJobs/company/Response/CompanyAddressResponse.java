package com.ajith.dailyJobs.company.Response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompanyAddressResponse {
    private Integer addressId;
    private String country;
    private String state;
    private String district;
    private String city;
    private String postalCode;
    private boolean isDeleted;
}
