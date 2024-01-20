package com.ajith.dailyJobs.company.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String country;
    private String state;
    private String district;
    private String city;
    private String postalCode;
}
