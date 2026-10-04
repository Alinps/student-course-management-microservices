package com.example.student_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final InternalServiceAuthenticationFilter internalServiceAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        /*
                        Internal endpoint currently used by Auth Service
                        permitAll() means Spring Security's user authentication is not
                        required for /student/internal.
                        But our:InternalServiceAuthenticationFilter
                        is now responsible for authenticating the calling service.
                        So:
                        /student/internal
                             │
                             ▼
                        InternalServiceAuthenticationFilter
                             │
                             ├── valid service credentials → continue
                             │
                             └── invalid → 401
                             We aren't making the endpoint publicly accessible anymore.
                         */

                        .requestMatchers("/student/internal").hasAuthority("SERVICE_EMPLOYEE_READ")
                        .requestMatchers("/student/internal/**").hasAuthority("SERVICE_EMPLOYEE_READ")

                        .requestMatchers("/student/all").hasRole("ADMIN")
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/actuator/metrics/**",
                                "/actuator/prometheus"
                        ).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        internalServiceAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }
}
