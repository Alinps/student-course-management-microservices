package com.example.api_gateway.config;


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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC ENDPOINTS
                        // =========================
                        .requestMatchers(
                                "/auth/login",
                                "/auth/register/student"
                        ).permitAll()

                        // =========================
                        // ADMIN ONLY
                        // =========================
                        .requestMatchers(
                                "/auth/register/employee"
                        ).hasRole("ADMIN")
                        .requestMatchers("/student/all")
                        .hasRole("ADMIN")

                        // =========================
                        // STUDENT SERVICE
                        // =========================
                        .requestMatchers(
                                "/student/**"
                        ).hasAnyRole("ADMIN", "STUDENT")

                        // =========================
                        // EMPLOYEE SERVICE
                        // =========================
                        .requestMatchers(
                                "/employee/**"
                        ).hasAnyRole("ADMIN", "EMPLOYEE")

                        // =========================
                        // COURSE SERVICE
                        // =========================
                        .requestMatchers(
                                "/course/**"
                        ).hasAnyRole("ADMIN","EMPLOYEE")

                        // =========================
                        // BATCH SERVICE
                        // =========================
                        .requestMatchers(
                                "/batch/**"
                        ).hasAnyRole("ADMIN","EMPLOYEE")

                        // =========================
                        // COURSE ASSIGNMENT
                        // =========================
                        .requestMatchers(
                                "/course-assignment/**"
                        ).hasAnyRole("ADMIN","EMPLOYEE")
                        // =========================
                        // STUDENT BATCH ASSIGNMENT
                        // =========================
                        .requestMatchers(
                                "/student-batch-assignment/**"
                        ).hasAnyRole("ADMIN","EMPLOYEE")
                        // =========================
                        // NOTES
                        // =========================
                        .requestMatchers(
                                "/notes/**"
                        ).hasAnyRole("ADMIN","EMPLOYEE","STUDENT","TRAINER")

                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/actuator/metrics/**",
                                "/actuator/prometheus"
                        ).permitAll()

                        // =========================
                        // EVERYTHING ELSE
                        // =========================
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return  http.build();

    }
}
