package com.example.course_assignment_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final InternalServiceAuthenticationFilter internalServiceAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // -------------------------
                        // ADMIN ONLY
                        // -------------------------
                        .requestMatchers(HttpMethod.POST, "/course-assignment/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/course-assignment/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/course-assignment/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/course-assignment/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/actuator/metrics/**",
                                "/actuator/prometheus"
                        ).permitAll()

                        // -------------------------
                        // ADMIN + TRAINER
                        // -------------------------
                        .requestMatchers(HttpMethod.GET, "/course-assignment/**"
                        ).hasAnyRole("ADMIN", "TRAINER")

                        // -------------------------
                        // EVERYTHING ELSE
                        // -------------------------
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
