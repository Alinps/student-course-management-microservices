package com.example.course_service.config;


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

                                // Internal service endpoint
                                .requestMatchers(HttpMethod.POST, "/course/internal/by-ids").hasAuthority("SERVICE_COURSE_READ")
                                .requestMatchers(HttpMethod.GET,"/course/internal/**").hasAnyAuthority("SERVICE_COURSE_READ")
                                .requestMatchers(HttpMethod.GET, "/course/technologies/internal/**").hasAnyAuthority("SERVICE_COURSE_READ")
                                .requestMatchers(
                                        "/actuator/health",
                                        "/actuator/info",
                                        "/actuator/metrics/**",
                                        "/actuator/prometheus"
                                ).permitAll()
                                .requestMatchers("/course/all").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/course/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/course/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/course/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH,"/course/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/course/**").hasAnyRole(
                                        "ADMIN", "TRAINER", "STUDENT"
                                )


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
