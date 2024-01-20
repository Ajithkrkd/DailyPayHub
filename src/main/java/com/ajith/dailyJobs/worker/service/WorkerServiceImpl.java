package com.ajith.dailyJobs.worker.service;

import com.ajith.dailyJobs.GlobalExceptionHandler.Exceptions.WorkerNotFoundException;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.config.JwtService;
import com.ajith.dailyJobs.worker.Requests.WorkerDetailsUpdateRequest;
import com.ajith.dailyJobs.worker.Response.WorkerDetailsResponse;
import com.ajith.dailyJobs.worker.entity.Worker;
import com.ajith.dailyJobs.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class WorkerServiceImpl implements WorkerService {

    private final JwtService jwtService;
    private final WorkerRepository workerRepository;
    public  final PasswordEncoder passwordEncoder;

    @Override
    public WorkerDetailsResponse getUserDetails(String token) throws WorkerNotFoundException {
        try {
            String userEmail = jwtService.extractUsername(token);
            String username = jwtService.extractUsername(token);
            Optional< Worker > userOptional = workerRepository.findByEmail(username);
            if (userOptional.isPresent()) {
                Worker existingWorker = userOptional.get();
                WorkerDetailsResponse workerDetailsResponse = new WorkerDetailsResponse ();
                workerDetailsResponse.setUserId ( existingWorker.getId () );
                workerDetailsResponse.setFirstName( existingWorker.getFirstName ());
                workerDetailsResponse.setLastName( existingWorker.getLastName ());
                workerDetailsResponse.setEmail( existingWorker.getEmail());
                workerDetailsResponse.setPhoneNumber ( existingWorker.getPhoneNumber ());
                workerDetailsResponse.setJoinDate ( String.valueOf ( existingWorker.getJoinDate () ) );
                workerDetailsResponse.setImageUrl ( Optional.ofNullable ( existingWorker.getProfileImagePath ( ) ) );
                return workerDetailsResponse;
            } else {
                throw new WorkerNotFoundException ("Worker not found");
            }
        } catch (WorkerNotFoundException e) {
            e.printStackTrace();
            throw new WorkerNotFoundException ("Error fetching worker details");
        }
    }

    @Override
    public void updateUserDetails (String token, WorkerDetailsUpdateRequest workerDetailsUpdateRequest) throws WorkerNotFoundException {
        try {
            String username = jwtService.extractUsername(token.substring ( 7 ));
            Optional< Worker > optionalUser = workerRepository.findByEmail(username);
            if (optionalUser.isPresent()) {
                Worker existingWorker = optionalUser.get();
                existingWorker.setFirstName ( workerDetailsUpdateRequest.getFirstName ());
                existingWorker.setLastName ( workerDetailsUpdateRequest.getLastName ());
                existingWorker.setEmail( workerDetailsUpdateRequest.getEmail());
                existingWorker.setPhoneNumber ( workerDetailsUpdateRequest.getPhoneNumber () );


                if( workerDetailsUpdateRequest.getPassword ().isPresent ()){System.out.println ( workerDetailsUpdateRequest.getPassword () +"ajith krkd");
                    String newPassword = workerDetailsUpdateRequest.getPassword ().get();
                    existingWorker.setPassword( passwordEncoder.encode ( newPassword ) );
                }else {
                    System.out.println ( existingWorker.getPassword () +"ajith" );
                    existingWorker.setPassword ( existingWorker.getPassword (  ));
                }

                workerRepository.save( existingWorker );
            } else {
                throw new WorkerNotFoundException ("Worker not found");
            }
        } catch (WorkerNotFoundException e) {
            // Handle exceptions and log the error
            e.printStackTrace();
            throw new WorkerNotFoundException ("Error updating worker details");
        }
    }



    public ResponseEntity < BasicResponse > blockUser(Long userId) {
        BasicResponse response = new BasicResponse();
        try {
            Optional < Worker > optionalUser = workerRepository.findById ( userId );

            if ( optionalUser.isPresent ( ) ) {
                Worker worker = optionalUser.get ( );
                worker.setIsActive ( !worker.getIsActive ( ) );
                workerRepository.save ( worker );
                response.setStatus ( HttpStatus.OK.value ( ) );
                response.setMessage ( worker.getIsActive ( ) ? "Worker " + worker.getFirstName ( ) + " is blocked" : "Worker " + worker.getFirstName ( ) + " is unblocked" );
                response.setDescription ( "worker status is updated" );
                response.setTimestamp ( LocalDateTime.now ( ) );
                return  ResponseEntity.ok(response);
            } else {
                throw new UsernameNotFoundException ( "Worker not found with id: " + userId );

            }

        }  catch(Exception e){
            return ResponseEntity.status ( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body ( new BasicResponse ( HttpStatus.INTERNAL_SERVER_ERROR.value ( ), LocalDateTime.now ( ) , e.getMessage ( ), "An error server side"  ));
        }
    }


    @Override
    public boolean isEmailExist (String email) {
        return workerRepository.existsByEmail ( email );

    }

    @Override
    public Optional < Worker > findUserByName (String userName) {
       return workerRepository.findByEmail ( userName );
    }

    @Override
    public String updateProfilePicture(String token, MultipartFile imageFile) throws WorkerNotFoundException {
        String userEmail = jwtService.extractUsername(token.substring(7));
        Optional< Worker > userOptional = workerRepository.findByEmail(userEmail);

        if (userOptional.isPresent()) {
            Worker worker = userOptional.get();
            try {
                String fileName = uploadImageAndSaveImagePathToUser(imageFile);
                worker.setProfileImagePath ("/uploads"+"/"+fileName);
                workerRepository.save( worker );
                return fileName;
            } catch (IOException e) {

                throw new RuntimeException("Failed to upload profile picture", e);
            }
        } else {
            throw new WorkerNotFoundException ("Worker not found for the given email: " + userEmail);
        }
    }

    @Override
    public void setTokenForVerification (String token, String email) {
        System.out.println ("setTokenForVerification "+token+" "+email);
        Optional< Worker > optionalUser = workerRepository.findByEmail(email);

        if( optionalUser.isPresent ( ) )
        {
            System.out.println (optionalUser.get ().getFirstName () + "from ajith rkr=-asdfkjhasfkphjasfdosjafdoijasflpihj" );
            Worker worker = optionalUser.get();
            worker.setEmailVerificationToken ( token );
            workerRepository.save ( worker );
        }
    }

    @Override
    public ResponseEntity< BasicResponse > confirmEmailwithToken (String token, String email) {

        Optional< Worker > optionalUser = workerRepository.findByEmail(email);
        if(optionalUser.isPresent ( ) ){
            Worker workerByEmail = optionalUser.get ();
                    Optional < Worker > optionalTokenContainingUser = workerRepository.findByEmailVerificationToken(token);
                    if(optionalTokenContainingUser.isPresent ())
                    {
                        Worker tokenContainingWorker = optionalTokenContainingUser.get ();
                        if( workerByEmail.equals ( tokenContainingWorker ))
                        {
                            workerByEmail.setEmailVerified ( true );
                            workerRepository.save ( workerByEmail );
                            return ResponseEntity.status ( HttpStatus.OK )
                                    .body ( BasicResponse.builder ()
                                            .message ( "Verification Success" )
                                            .description ( "Verification success with token worker is confirmed" )
                                            .status ( HttpStatus.OK.value ( ) )
                                            .timestamp ( LocalDateTime.now () )
                                            .build ()
                                    );
                        }
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
        else {
            return ResponseEntity.status ( HttpStatus.NOT_FOUND )
                    .body ( BasicResponse.builder ()
                            .message ( "Verification Failed" )
                            .description ( "Verification failed with Email not found" )
                            .status ( HttpStatus.NOT_FOUND.value ( ) )
                            .timestamp ( LocalDateTime.now () )
                            .build ()
                    );
        }

        return ResponseEntity.status ( HttpStatus.INTERNAL_SERVER_ERROR )
                .body ( BasicResponse.builder ()
                        .message ( "Verification Failed" )
                        .description ( "Verification failed with Server side Error " )
                        .status ( HttpStatus.INTERNAL_SERVER_ERROR.value ( ) )
                        .timestamp ( LocalDateTime.now () )
                        .build ()
                );
    }

    private String uploadImageAndSaveImagePathToUser(MultipartFile imageFile) throws IOException {
        String rootPath = System.getProperty("user.dir");
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
}