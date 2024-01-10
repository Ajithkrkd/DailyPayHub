package com.ajith.dailyJobs.user.service;
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
import org.springframework.web.server.ResponseStatusException;

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
                existingUser.setFirstName (userDetailsUpdateRequest.getFirtName ());
                existingUser.setLastName (userDetailsUpdateRequest.getLastName ());
                existingUser.setEmail(userDetailsUpdateRequest.getEmail());


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


}