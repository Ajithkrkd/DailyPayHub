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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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


            Optional < Worker > optionalWorker = workerRepository.findById ( workerId );
            if ( optionalWorker.isPresent()) {
                Worker existingWorker = optionalWorker.get ();
                Optional<Company> optionalCompany = Optional.ofNullable ( existingWorker.getCompany ( ) );

                if(optionalCompany.isPresent ()) {
                    Company existingCompany = optionalCompany.get ();
                    Company allDetailsUpdatedCompany = setCompanyDetailsForRegistration (
                            companyRegisterRequest,existingCompany,existingWorker );
                    companyRepository.save ( allDetailsUpdatedCompany );
                }else {
                    Company newCompany = new Company ( );
                    Company allDetailsUpdatedCompany = setCompanyDetailsForRegistration (
                            companyRegisterRequest,newCompany,existingWorker );
                    companyRepository.save ( allDetailsUpdatedCompany );
                }

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
                                    .companyLogoUrl (existingCompany.getCompanyLogoUrl () )
                                    .companyNumber ( existingCompany.getCompanyNumber () )
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

    @Override
    public void uploadCompanyLogo (MultipartFile imageFile, Long workerId) throws WorkerNotFoundException, IOException {
        Optional<Worker> optionalWorker = workerRepository.findById ( workerId );

        if (optionalWorker.isPresent()) {
            Worker worker = optionalWorker.get();
           Optional<Company> optionalCompany = Optional.ofNullable ( worker.getCompany ( ) );
           if(optionalCompany.isPresent()) {
               String fileName = uploadImageToLocalDirAndReturnFileName(imageFile);
               Company existingCompany = worker.getCompany ();
               existingCompany.setCompanyLogoUrl ( "/uploads/"+fileName );
               companyRepository.save ( existingCompany );
           }
           else {
               Company company = new Company();
               String fileName = uploadImageToLocalDirAndReturnFileName(imageFile);
               company.setCompanyLogoUrl ("/uploads/"+fileName);
               company.setWorker ( worker );
               companyRepository.save ( company );
           }
        } else {
            throw new WorkerNotFoundException ("Worker not found for the given worker id : " + workerId);
        }
    }

    private String uploadImageToLocalDirAndReturnFileName (MultipartFile imageFile) throws IOException {
        String rootPath = System.getProperty ( "user.dir" );
        String uploadDir = rootPath + "/src/main/resources/static/uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = imageFile.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;
        Path path = Paths.get(filePath);
        try {
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            // Handle the file copy exception
            throw new IOException("Failed to copy profile picture file", e);
        }
    }

    private static Company setCompanyDetailsForRegistration (CompanyRegisterRequest companyRegisterRequest,Company newCompany, Worker worker) {
        newCompany.setCompanyEmail ( companyRegisterRequest.getCompanyEmail ( ) );
        newCompany.setCompanyName ( companyRegisterRequest.getCompanyName ( ) );
        newCompany.setCompanyOwnerName ( companyRegisterRequest.getCompanyOwnerName ( ) );
        newCompany.setCompanyNumber ( companyRegisterRequest.getCompanyNumber ( ) );
        newCompany.setWorker ( worker );
        return newCompany;
    }
}
