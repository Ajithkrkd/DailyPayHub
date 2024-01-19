package com.ajith.dailyJobs.company.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.*;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.company.CompanyRegisterRequest;
import com.ajith.dailyJobs.company.Response.CompanyResponse;
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
            CompanyRegisterRequest companyRegisterRequest ,Long workerId)
            throws WorkerNotFoundException {
        try {

            boolean existCompanyName = companyRepository.existsByCompanyName(companyRegisterRequest.getCompanyName ());
            boolean existEmail = companyRepository.existsByCompanyEmail (companyRegisterRequest.getCompanyEmail ());

            if(existCompanyName)
            {
                throw new CompanyNameAlreadyExistsException ("This company name already exists");
            }

            if (existEmail) {
               throw new EmailAlreadyExistsException ("email already exists");
            }


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
        catch ( CompanyNameAlreadyExistsException e ){
            throw new CompanyNameAlreadyExistsException ( e.getMessage() );
        }
        catch (WorkerNotFoundException e)
        {
            throw  new WorkerNotFoundException ( "Worker not found" );
        }
        catch (EmailAlreadyExistsException e) {
            throw new RuntimeException ( e );
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

    @Override
    public void setTokenForVerification (String token, String email) {
        try{
            Optional < Company > company = companyRepository.findByCompanyEmail (email);
            if(company.isPresent ()) {
                Company existingCompany = company.get ();
                existingCompany.setEmailToken ( token );
                companyRepository.save ( existingCompany );
            }
        }
        catch (EmailNotVerifiedException e)
        {
            throw new EmailNotVerifiedException ( e.getMessage() );
        }
    }

    @Override
    public ResponseEntity < BasicResponse > confirmEmailWithToken (String token) {


            Optional < Company > optionalTokenContainingCompany = companyRepository.findByEmailToken(token);
            if(optionalTokenContainingCompany.isPresent ())
            {
                Company tokenContainingCompany = optionalTokenContainingCompany.get ();

                tokenContainingCompany.setCompanyEmailVerified ( true );
                    companyRepository.save ( tokenContainingCompany );
                    return ResponseEntity.status ( HttpStatus.OK )
                            .body ( BasicResponse.builder ()
                                    .message ( "Verification Success" )
                                    .description ( "Verification success with token company is confirmed" )
                                    .status ( HttpStatus.OK.value ( ) )
                                    .timestamp ( LocalDateTime.now () )
                                    .build ()
                            );

            }else{
                return ResponseEntity.status ( HttpStatus.NOT_FOUND )
                        .body ( BasicResponse.builder ()
                                .message ( "Verification Failed" )
                                .description ( "Verification failed with token not found" )
                                .status ( HttpStatus.NOT_FOUND.value ( ) )
                                .timestamp ( LocalDateTime.now () )
                                .build ()
                        );
            }
    }

    @Override
    public ResponseEntity < CompanyResponse > getCompanyDetailsByWorkerId (Long workerId)
            throws WorkerNotFoundException, CompanyNotFountException, InternalServerException {
        try {
            Optional<Worker> optionalWorker = workerRepository.findById(workerId);
            if (optionalWorker.isPresent()) {
                Worker existingWorker = optionalWorker.get();
                Optional<Company> optionalCompany = companyRepository.findByWorkerId (existingWorker.getId ());
                if(optionalCompany.isPresent ()) {
                    Company existingCompany = optionalCompany.get ();
                    return  ResponseEntity.status ( HttpStatus.OK.value ( )).body (
                            CompanyResponse
                                    .builder ( )
                                    .companyId ( existingCompany.getCompanyId ( ) )
                                    .companyEmail (existingCompany.getCompanyEmail () )
                                    .companyName ( existingCompany.getCompanyName() )
                                    .companyOwnerName ( existingCompany.getCompanyOwnerName() )
                                    .isCompanyDocumentVerified ( existingCompany.isCompanyDocumentVerified () )
                                    .isCompanyEmailVerified ( existingCompany.isCompanyEmailVerified() )
                                    .build ( ) );
                }
                else {
                throw new CompanyNotFountException ( "Worker " + existingWorker.getFirstName () + "Have no company" );
                }
            }
            else{
                throw new WorkerNotFoundException ( "Worker " + workerId + "is not found");
            }

        } catch (WorkerNotFoundException e) {
            throw new WorkerNotFoundException (e.getMessage ());

        } catch (CompanyNotFountException e) {
            throw new CompanyNotFountException ( e.getMessage () );
        }catch (Exception e)
        {   e.printStackTrace ();
            throw new InternalServerException ( "server error" );
        }
    }

    private static Company getCompany (CompanyRegisterRequest companyRegisterRequest, Optional < Worker > existingWorker) {
        Worker worker = existingWorker.get();
        System.out.println (worker.getFirstName () + "--------------------------------------- " );
        Company newCompany = new Company ( );
        newCompany.setCompanyEmail ( companyRegisterRequest.getCompanyEmail ( ) );
        newCompany.setCompanyName ( companyRegisterRequest.getCompanyName ( ) );
        newCompany.setCompanyOwnerName ( companyRegisterRequest.getCompanyOwnerName ( ) );
        newCompany.setCompanyNumber ( companyRegisterRequest.getCompanyNumber ( ) );
        newCompany.setWorker ( worker );
        return newCompany;
    }
}
