package com.ajith.dailyJobs.user.service;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.config.JwtService;
import com.ajith.dailyJobs.user.Exceptions.CustomAuthenticationException;
import com.ajith.dailyJobs.user.Requests.UserDetailsUpdateRequest;
import com.ajith.dailyJobs.user.Response.UserDetailsResponse;
import com.ajith.dailyJobs.user.entity.User;
import com.ajith.dailyJobs.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    public  final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsResponse getUserDetails(String token) {
        try {
            String userEmail = jwtService.extractUsername(token);
            String username = jwtService.extractUsername(token);
            Optional< User > userOptional = userRepository.findByEmail(username);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
                userDetailsResponse.setFirstName(existingUser.getFirstName ());
                userDetailsResponse.setLastName(existingUser.getLastName ());
                userDetailsResponse.setEmail(existingUser.getEmail());
                userDetailsResponse.setPhoneNumber ( existingUser.getPhoneNumber ());
                userDetailsResponse.setJoinDate ( String.valueOf ( existingUser.getJoinDate () ) );
                userDetailsResponse.setImageUrl ( Optional.ofNullable ( existingUser.getProfileImagePath ( ) ) );
                return userDetailsResponse;
            } else {
                throw new CustomAuthenticationException ("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomAuthenticationException ("Error fetching user details");
        }
    }

    @Override
    public void updateUserDetails (String token, UserDetailsUpdateRequest userDetailsUpdateRequest) {
        try {
            String username = jwtService.extractUsername(token.substring ( 7 ));
            Optional<User> optionalUser = userRepository.findByEmail(username);
            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();
                existingUser.setFirstName (userDetailsUpdateRequest.getFirstName ());
                existingUser.setLastName (userDetailsUpdateRequest.getLastName ());
                existingUser.setEmail(userDetailsUpdateRequest.getEmail());
                existingUser.setPhoneNumber ( userDetailsUpdateRequest.getPhoneNumber () );


                if(userDetailsUpdateRequest.getPassword ().isPresent ()){System.out.println (userDetailsUpdateRequest.getPassword () +"ajith krkd");
                    String newPassword = userDetailsUpdateRequest.getPassword ().get();
                    existingUser.setPassword( passwordEncoder.encode ( newPassword ) );
                }else {
                    System.out.println (existingUser.getPassword () +"ajith" );
                    existingUser.setPassword (existingUser.getPassword (  ));
                }

                userRepository.save(existingUser);
            } else {
                // User not found
                throw new CustomAuthenticationException ("User not found");
            }
        } catch (Exception e) {
            // Handle exceptions and log the error
            e.printStackTrace();
            throw new CustomAuthenticationException ("Error updating user details");
        }
    }



    public ResponseEntity < String > blockUser(Long userId) {
        try {
            Optional < User > optionalUser = userRepository.findById ( userId );
            if ( optionalUser.isPresent ( ) ) {
                User user = optionalUser.get ( );
                user.setIsActive ( !user.getIsActive () );
                userRepository.save(user);
            } else {
                throw new UsernameNotFoundException ( "User not found with id: " + userId );
            }
        } catch (Exception e) {
            throw new ResponseStatusException ( HttpStatus.INTERNAL_SERVER_ERROR, "Error blocking user", e);
        }
        return null;
    }

    @Override
    public boolean isEmailExist (String email) {
        return userRepository.existsByEmail ( email );

    }

    @Override
    public Optional < User > findUserByName (String userName) {
       return userRepository.findByEmail ( userName );
    }

    @Override
    public String updateProfilePicture(String token, MultipartFile imageFile) {
        String userEmail = jwtService.extractUsername(token.substring(7));
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                String fileName = uploadImageAndSaveImagePathToUser(imageFile);
                user.setProfileImagePath ("/uploads"+"/"+fileName);
                userRepository.save(user);
                return fileName;
            } catch (IOException e) {

                throw new RuntimeException("Failed to upload profile picture", e);
            }
        } else {

            throw new RuntimeException("User not found for the given email: " + userEmail);
        }
    }

    @Override
    public void setTokenForVerification (String token, String email) {
        System.out.println ("setTokenForVerification "+token+" "+email);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if( optionalUser.isPresent ( ) )
        {
            System.out.println (optionalUser.get ().getFirstName () + "from ajith rkr=-asdfkjhasfkphjasfdosjafdoijasflpihj" );
            User user = optionalUser.get();
            user.setEmailVerificationToken ( token );
            userRepository.save ( user );
        }
    }

    @Override
    public ResponseEntity< BasicResponse > cofirmEmailwithToken (String token, String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent ( ) ){
            User  userByEmail = optionalUser.get ();
                    Optional < User > optionalTokenContainingUser = userRepository.findByEmailVerificationToken(token);
                    if(optionalTokenContainingUser.isPresent ())
                    {
                        User tokenContainingUser = optionalTokenContainingUser.get ();
                        if(userByEmail.equals ( tokenContainingUser ))
                        {
                            return ResponseEntity.status ( HttpStatus.OK )
                                    .body ( BasicResponse.builder ()
                                            .message ( "Verification Success" )
                                            .description ( "Verification success with token user is confirmed" )
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
            System.out.println("Profile picture is uploaded: " + fileName);
            return fileName;
        } catch (IOException e) {
            // Handle the file copy exception
            throw new IOException("Failed to copy profile picture file", e);
        }
    }
}