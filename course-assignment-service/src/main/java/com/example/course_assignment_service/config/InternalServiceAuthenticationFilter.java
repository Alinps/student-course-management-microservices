package com.example.course_assignment_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class InternalServiceAuthenticationFilter extends OncePerRequestFilter {

    @Value("${internal.auth.service-name}")
    private String expectedServiceName;

    @Value("${internal.auth.service-key}")
    private String expectedServiceKey;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();

        boolean internalEndpoint =
                uri.equals("/employee/internal")
                        || uri.equals("/employee/by-ids");

        if (!internalEndpoint) {
            filterChain.doFilter(request, response);
            return;
        }


        String serviceName = request.getHeader("X-service-Name");
        String serviceKey = request.getHeader("X-service-Key");

        if (!expectedServiceName.equals(serviceName) || !expectedServiceKey.equals(serviceKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        serviceName,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "SERVICE_COURSE_ASSIGNMENT_READ"
                                )
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request,response);
    }

}
