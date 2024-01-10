package com.ajith.dailyJobs.config;
import com.ajith.dailyJobs.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http.csrf ( AbstractHttpConfigurer::disable )

                .authorizeHttpRequests ( auth -> {
                    auth.requestMatchers ( "/api/auth/**")
                            .permitAll ( )
                            .requestMatchers ( "/api/admin/**" ).hasAuthority ( String.valueOf ( Role.ADMIN ) )
                            .requestMatchers ( "/api/users/**" ).hasAuthority ( String.valueOf ( Role.USER ) )
                            .anyRequest ( ).authenticated ( );

                } )

                .sessionManagement ( session -> session.sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
                .authenticationProvider ( authenticationProvider )
                .addFilterBefore ( jwtAuthFilter, UsernamePasswordAuthenticationFilter.class )
                .build ( );


    }
}