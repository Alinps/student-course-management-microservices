package com.example.batch_service.config;


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
                        .requestMatchers(HttpMethod.POST ,"/batch/internal/by-ids")
                        .hasAuthority("SERVICE_BATCH_READ")
                        .requestMatchers(HttpMethod.GET, "/batch/internal/**")
                        .hasAuthority("SERVICE_BATCH_READ")

                        .requestMatchers(HttpMethod.POST, "/batch/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/batch/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/batch/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/batch/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/batch/**")
                        .hasAnyRole("ADMIN", "TRAINER")

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
