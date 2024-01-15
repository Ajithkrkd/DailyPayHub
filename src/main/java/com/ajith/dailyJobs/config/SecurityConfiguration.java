package com.ajith.dailyJobs.config;
import com.ajith.dailyJobs.worker.Role;
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
    private static final String[] WHITE_LIST_URL = {"/api/auth/**","/uploads/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http.csrf ( AbstractHttpConfigurer::disable )

                .authorizeHttpRequests ( auth -> {
                    auth.requestMatchers ( WHITE_LIST_URL)
                            .permitAll ( )
                            .requestMatchers ( "/api/admin/**" ).hasAuthority ( String.valueOf ( Role.ADMIN ) )
                            .requestMatchers ( "/api/worker/**" ).hasAuthority ( String.valueOf ( Role.WORKER ) )
                            .anyRequest ( ).authenticated ();

                } )

                .sessionManagement ( session -> session.sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
                .authenticationProvider ( authenticationProvider )
                .addFilterBefore ( jwtAuthFilter, UsernamePasswordAuthenticationFilter.class )
                .build ( );


    }
}