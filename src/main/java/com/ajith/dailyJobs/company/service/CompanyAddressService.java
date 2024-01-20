package com.ajith.dailyJobs.company.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.company.Requests.AddressRequest;

public interface CompanyAddressService {
    void createAddress (AddressRequest addressRequest, Integer companyId) throws CompanyNotFountException;
}
