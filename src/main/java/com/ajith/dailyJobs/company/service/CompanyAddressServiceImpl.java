package com.ajith.dailyJobs.company.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.company.Requests.AddressRequest;
import com.ajith.dailyJobs.company.entity.Company;
import com.ajith.dailyJobs.company.entity.CompanyAddress;
import com.ajith.dailyJobs.company.repository.CompanyAddressRepository;
import com.ajith.dailyJobs.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyAddressServiceImpl implements  CompanyAddressService{

    private  final CompanyRepository companyRepository;
    private  final CompanyAddressRepository companyAddressRepository;

    @Override
    public void createAddress (AddressRequest addressRequest, Integer companyId) throws CompanyNotFountException {
        try {
            Optional< Company > optionalCompany = companyRepository.findByCompanyId ( companyId );
            if(optionalCompany.isPresent()) {
                    Company company = optionalCompany.get();
                CompanyAddress newAddress = new CompanyAddress ();
                newAddress.setCompany ( company );
                newAddress.setCity (addressRequest.getCity () );
                newAddress.setCountry ( addressRequest.getCountry () );
                newAddress.setState ( addressRequest.getState());
                newAddress.setPostalCode (addressRequest.getPostalCode ());
                newAddress.setDistrict ( addressRequest.getDistrict () );
                newAddress.setDeleted ( false );
                companyAddressRepository.save ( newAddress );

            }
            else {
                throw new CompanyNotFountException ( "Could not find company with this id -- "+companyId );
            }
        } catch (CompanyNotFountException e) {
            throw new CompanyNotFountException ( e.getMessage () );
        }
    }
}
