package com.example.note_service.config;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws ServletException {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth


                        // Create notes
                        .requestMatchers(HttpMethod.POST,"/notes").hasAnyRole("ADMIN","TRAINER","EMPLOYEE")

                        // Read notes
                        .requestMatchers(HttpMethod.GET, "/notes/**").hasAnyRole("ADMIN","EMPLOYEE","STUDENT")

                        // Update notes
                        .requestMatchers(HttpMethod.PUT, "/notes/**").hasAnyRole("ADMIN","TRAINER","EMPLOYEE")

                        // Patch notes
                        .requestMatchers(HttpMethod.PATCH, "/notes/**").hasAnyRole("ADMIN","TRAINER","EMPLOYEE")

                        // Delete notes
                        .requestMatchers(HttpMethod.DELETE, "/notes/**").hasAnyRole("ADMIN","TRAINER","EMPLOYEE")

                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/actuator/metrics/**",
                                "/actuator/prometheus"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }
}
