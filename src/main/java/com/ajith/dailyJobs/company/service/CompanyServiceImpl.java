package com.ajith.dailyJobs.company.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.WorkerNotFoundException;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.company.CompanyRegisterRequest;
import com.ajith.dailyJobs.company.entity.Company;
import com.ajith.dailyJobs.company.repository.CompanyRepository;
import com.ajith.dailyJobs.worker.entity.Worker;
import com.ajith.dailyJobs.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final WorkerRepository workerRepository;
    @Override
    public ResponseEntity < BasicResponse > registerCompany (
            CompanyRegisterRequest companyRegisterRequest ,Long workerId) throws WorkerNotFoundException {
        try {
            Optional < Worker > existingWorker = workerRepository.findById ( workerId );
            if ( existingWorker.isPresent()) {
                Company newCompany = getCompany ( companyRegisterRequest, existingWorker );
                companyRepository.save ( newCompany );
                return ResponseEntity.status ( HttpStatus.CREATED )
                        .body (
                                BasicResponse
                                        .builder ( )
                                        .timestamp ( LocalDateTime.now ( ) )
                                        .status ( HttpStatus.CREATED.value ( ) )
                                        .description ( "company created successfully" )
                                        .message ( "success" )
                                        .build ( ) );
            }
            else{
                throw new WorkerNotFoundException ( "Worker not found");
            }
        }
        catch (WorkerNotFoundException e)
        {
            throw  new WorkerNotFoundException ( "Worker not found" );
        }
        catch ( Exception e )
        {
            return ResponseEntity.status ( HttpStatus.INTERNAL_SERVER_ERROR)
                    .body ( BasicResponse
                            .builder ()
                            .status ( HttpStatus.INTERNAL_SERVER_ERROR.value() )
                            .message ( "error" )
                            .description ( "An Server Error occurred" )
                            .build ());
        }
    }

    private static Company getCompany (CompanyRegisterRequest companyRegisterRequest, Optional < Worker > existingWorker) {
        Worker worker = existingWorker.get();
        Company newCompany = new Company ( );
        newCompany.setCompanyEmail ( companyRegisterRequest.getCompanyEmail ( ) );
        newCompany.setCompanyName ( companyRegisterRequest.getCompanyName ( ) );
        newCompany.setCompanyOwnerName ( companyRegisterRequest.getCompanyOwnerName ( ) );
        newCompany.setCompanyNumber ( companyRegisterRequest.getCompanyNumber ( ) );
        newCompany.setWorker ( worker );
        return newCompany;
    }
}
