package com.ajith.dailyJobs.company.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyAddressNotFountException;
import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.CompanyNotFountException;
import com.ajith.dailyJobs.company.Requests.AddressRequest;
import com.ajith.dailyJobs.company.Response.CompanyAddressResponse;
import com.ajith.dailyJobs.company.entity.Company;
import com.ajith.dailyJobs.company.entity.CompanyAddress;
import com.ajith.dailyJobs.company.repository.CompanyAddressRepository;
import com.ajith.dailyJobs.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyAddressServiceImpl implements  CompanyAddressService{

    private  final CompanyRepository companyRepository;
    private  final CompanyAddressRepository companyAddressRepository;


    @Override
    public void createAddress(AddressRequest addressRequest, Integer companyId) throws CompanyNotFountException {
        try {
            Optional<Company> optionalCompany = companyRepository.findByCompanyId(companyId);
            if (optionalCompany.isPresent()) {
                Company existingCompany = optionalCompany.get();
                Optional<CompanyAddress> optionalCompanyAddress = Optional.ofNullable ( existingCompany.getCompanyAddress ( ) );
                if(optionalCompanyAddress.isPresent ()){
                    CompanyAddress existingCompanyAddress = optionalCompanyAddress.get();
                    saveAndReturnCompanyAddress ( addressRequest, existingCompanyAddress );
                    existingCompany.setCompanyAddress(existingCompanyAddress);
                    companyRepository.save(existingCompany);
                }else{
                    CompanyAddress newCompanyAddress = new CompanyAddress();
                    saveAndReturnCompanyAddress ( addressRequest, newCompanyAddress );
                    existingCompany.setCompanyAddress(newCompanyAddress);
                    companyRepository.save(existingCompany);
                }

            } else {
                throw new CompanyNotFountException ("Could not find company with this id -- " + companyId);
            }
        } catch (CompanyNotFountException e) {
            throw new CompanyNotFountException(e.getMessage());
        }
    }

    /**
     * @param companyId
     */
    @Override
    public ResponseEntity< CompanyAddressResponse> getCompanyAddress (Integer companyId) throws CompanyAddressNotFountException, CompanyNotFountException {
        try {
            Optional<Company> existingCompany = companyRepository.findByCompanyId(companyId);
            if(existingCompany.isPresent()) {
                Optional<CompanyAddress> existingAddress = Optional.ofNullable ( existingCompany.get ( ).getCompanyAddress ( ) );
                if(existingAddress.isPresent()) {
                    CompanyAddress address = existingAddress.get();
                    return ResponseEntity.status ( HttpStatus.OK ).body ( CompanyAddressResponse
                            .builder ()
                                .city ( address.getCity ()  )
                                .country ( address.getCountry())
                                .state ( address.getState())
                                .state ( address.getPostalCode())
                                .district ( address.getDistrict())
                                .isDeleted ( address.isDeleted ( ) )
                                .build ()
                    );

                }else {
                    throw new CompanyAddressNotFountException ( "Company address not fount" );
                }

            }
            else {
                throw new CompanyNotFountException ( "company with this id is not present" );

            }

        } catch (CompanyNotFountException e) {
            throw new CompanyNotFountException ( e.getMessage () );
        } catch (CompanyAddressNotFountException e) {
            throw new CompanyAddressNotFountException ( e.getMessage () );
        }
    }

    private void saveAndReturnCompanyAddress (AddressRequest addressRequest, CompanyAddress existingCompanyAddress) {
        existingCompanyAddress.setCity( addressRequest.getCity());
        existingCompanyAddress.setCountry( addressRequest.getCountry());
        existingCompanyAddress.setState( addressRequest.getState());
        existingCompanyAddress.setPostalCode( addressRequest.getPostalCode());
        existingCompanyAddress.setDistrict( addressRequest.getDistrict());
        existingCompanyAddress.setDeleted(false);
        companyAddressRepository.save( existingCompanyAddress );
    }

}
