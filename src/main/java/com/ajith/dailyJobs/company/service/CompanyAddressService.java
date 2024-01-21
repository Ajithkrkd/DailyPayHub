package com.ajith.dailyJobs.company.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyAddressNotFountException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.company.Requests.AddressRequest;
import com.ajith.dailyJobs.company.Response.CompanyAddressResponse;
import org.springframework.http.ResponseEntity;

public interface CompanyAddressService {
    void createAddress (AddressRequest addressRequest, Integer companyId) throws CompanyNotFountException;

    ResponseEntity < CompanyAddressResponse > getCompanyAddress (Integer companyId) throws CompanyAddressNotFountException, CompanyNotFountException;
}
