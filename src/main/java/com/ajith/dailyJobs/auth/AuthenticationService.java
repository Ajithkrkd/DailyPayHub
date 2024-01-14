package com.ajith.dailyJobs.auth;
import com.ajith.dailyJobs.auth.Exceptions.EmailNotVerifiedException;
import com.ajith.dailyJobs.auth.Exceptions.UserBlockedException;
import com.ajith.dailyJobs.auth.Requests.AuthenticationRequest;
import com.ajith.dailyJobs.auth.Requests.RegisterRequest;
import com.ajith.dailyJobs.auth.Response.AuthenticationResponse;
import com.ajith.dailyJobs.common.BasicResponse;
import com.ajith.dailyJobs.config.JwtService;
import com.ajith.dailyJobs.token.Token;
import com.ajith.dailyJobs.token.TokenRepository;
import com.ajith.dailyJobs.token.TokenType;
import com.ajith.dailyJobs.worker.Role;
import com.ajith.dailyJobs.worker.entity.Worker;
import com.ajith.dailyJobs.worker.repository.WorkerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final WorkerRepository workerRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    public ResponseEntity<BasicResponse> register (RegisterRequest request) {
        System.out.println (request );
        var user = Worker.builder ( )
                .firstName ( request.getFirstName () )
                .lastName ( request.getLastName () )
                .phoneNumber ( request.getPhoneNumber () )
                .email ( request.getEmail () )
                .password (passwordEncoder.encode (request.getPassword ()))
                .joinDate ( Date.from ( Instant.now () ))
                .role (  Role.WORKER )
                .build ();
        Worker savedWorker = workerRepository.save ( user );
        return ResponseEntity.status( HttpStatus.CREATED)
                .body(BasicResponse.builder()
                        .message ("Worker Registered successfully")
                        .description ( "The worker has been registered " )
                        .timestamp ( LocalDateTime.now () )
                        .status ( HttpStatus.CREATED.value ( ) )
                        .build());

    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );


            var user = workerRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Worker not found"));

            if (user.getIsActive ()) {
                throw new UserBlockedException ("Worker is blocked");
            }
            if(!user.isEmailVerified ())
            {
                System.out.println ("Worker is not----------------------------------------" );
                throw new EmailNotVerifiedException ("email verification failed");
            }
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken ( user );

            revokeAllTokens ( user );
            saveUserToken ( user, refreshToken );


            return
                    AuthenticationResponse.builder()
                    .accessToken (jwtToken)
                    .refreshToken ( refreshToken )
                    .build();
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException ("Password is Wrong");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Worker not found");
        }
    }

    public void revokeAllTokens(Worker worker) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser ( worker.getId () );
        if(validUserTokens.isEmpty()) {
            return;
        }
        else{
                validUserTokens.forEach ( t ->{
                    t.setRevoked ( true );
                    t.setExpired ( true );}
                 );
            System.out.println ("Valid ---------------------------" );
                tokenRepository.saveAll ( validUserTokens );
        }
    }


    public AuthenticationResponse refreshToken (
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final  String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return AuthenticationResponse.builder()
                    .message ( "Invalid Authorization header" )
                    .build();
        }


        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail != null ){

             Optional < Worker > existingUser = workerRepository.findByEmail ( userEmail );
                if(existingUser.isPresent ()){
                    Worker worker = existingUser.get();

                    var isTokenValid = tokenRepository.findByToken ( refreshToken ).
                            map ( token -> token.isRefreshToken ( ) &&
                                    !token.isRevoked () &&
                                    !token.isExpired () )
                            .orElse ( false );


                    if(jwtService.isTokenValid ( refreshToken, worker ) && isTokenValid ){
                      var accessToken = jwtService.generateToken ( worker );
                      var newRefreshToken = jwtService.generateRefreshToken( worker );

                          revokeAllTokens ( worker );
                          saveUserToken ( worker,newRefreshToken );

                          return AuthenticationResponse.builder ()
                                  .refreshToken ( newRefreshToken )
                                  .accessToken ( accessToken )
                                  .build ();

                    }
                }
        }

        return AuthenticationResponse.builder()
                .message ("Token is not valid Please Login or Register")
        .build();
    }
    private void saveUserToken (Worker worker, String jwtToken) {
        var token = Token.builder ( )
                .worker ( worker )
                .token ( jwtToken )
                .tokenType ( TokenType.BEARER )
                .isRefreshToken ( true )
                .expired ( false )
                .revoked ( false )
                .build ();
        tokenRepository.save ( token );
    }

    public void  sentMailForVerification (String email, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("DilyPayHub@gmail.com" , "DailyPayHub");
        helper.setTo(email);

        String subject = "Here's the link to verify your email";
        String content = "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #007bff;'>Verify Your Email</h2>"
                + "<p>Hello,</p>"
                + "<p>You have requested to verify your email.</p>"
                + "<p><a href='" + link + "' style='background-color: #007bff; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Click here to verify your email</a></p>"
                + "<p>If the above link doesn't work, you can copy and paste this URL into your browser:</p>"
                + "<p><a href='" + link + "'>" + link + "</a></p>"
                + "<p style='color: #888;'>Ignore this email if you remember your password or if you haven't made this request.</p>"
                + "</body></html>";


        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
    }


}