package com.ajith.dailyJobs.company.entity;

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
public class CompanyAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;
    private String country;
    private String state;
    private String district;
    private String city;
    private String postalCode;
    private boolean isDeleted;

    @OneToOne(mappedBy = "companyAddress")
    private Company company;
}
