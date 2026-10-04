package com.example.employee_service.config;


import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final InternalServiceAuthenticationFilter internalServiceAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .authorizeHttpRequests(auth -> auth

                        // Auth Service → Employee Service
                        .requestMatchers("/employee/internal",  "/employee/internal/by-ids").hasAuthority("SERVICE_EMPLOYEE_READ")
                        .requestMatchers(HttpMethod.GET, "/employee/internal/**").hasAuthority("SERVICE_EMPLOYEE_READ")
                        .requestMatchers(HttpMethod.POST, "/employee/internal/by-ids").hasAuthority("SERVICE_EMPLOYEE_READ")

                        // Only ADMIN
                        .requestMatchers("/employee/all").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,"/employee/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,"/employee/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/actuator/metrics/**",
                                "/actuator/prometheus"
                        ).permitAll()

                        .anyRequest().authenticated()

                )

                .addFilterBefore(
                        internalServiceAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();

    }

}
