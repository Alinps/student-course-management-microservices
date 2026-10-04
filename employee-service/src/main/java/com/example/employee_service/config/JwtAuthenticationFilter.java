package com.example.employee_service.config;


import com.example.employee_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authenticationHeader = request.getHeader("Authorization");

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {

            log.debug(
                    "No Bearer token found. method={}, path={}",
                    request.getMethod(),
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);
            return;
        }

        String token = authenticationHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {

            log.warn(
                    "Invalid or expired JWT. method={}, path={}",
                    request.getMethod(),
                    request.getRequestURI()
            );


            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long userId = jwtService.extractUserId(token);
        String username = jwtService.extractUsername(token);
        List<String> roles =  jwtService.extractRoles(token);


        /*
        SimpleGrantedAuthority wraps raw role strings into the format Spring Security requires.
        By prepending "ROLE_", it allows you to use standard method-level annotations
        like @PreAuthorize("hasRole('ADMIN')"), which automatically checks for ROLE_ADMIN.
        */
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();


         /*
        Creates a standard Spring Security Authentication object
        holding three core pieces of identity:

        principal (userId): Who the user is.

        credentials (null): Left null because JWT authentication is stateless and
        password verification has already occurred.

        authorities: The user's roles/permissions.

        Reason: Spring Security needs a unified interface to represent authenticated
        entities. Using the constructor with 3 parameters also automatically marks this
        token as authenticated = true.
         */
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                authorities
        );


         /*
        Use: Captures web-specific metadata about the HTTP request,
        such as the user's remote IP address and session ID (if present).

        Reason: Provides optional contextual details that can be useful for
        audit logs, security checks, or rate-limiting later in the filter chain.
         */
        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );

         /*
        This line of code is the exact moment Spring Security registers the user as
        successfully logged in for the current HTTP request.

        Why It Is Used
        Establishes Identity: It saves the Authentication object (containing the userId and
        authorities) into Spring Security's memory space for the current thread.

        Unlocks Protected Routes: Spring Security checks SecurityContextHolder
        downstream to decide whether to allow or block the request.
        If this line does not run, Spring treats the request as unauthenticated and throws
        a 401 Unauthorized or 403 Forbidden error.

        How It Works Behind the Scenes
        ThreadLocal Storage: SecurityContextHolder uses ThreadLocal under the hood.
        This means the authentication context is bound specifically to the single thread
        executing this HTTP request.

        Access Anywhere: Any component downstream (Controllers, Services, or custom
        Security Evaluators) can retrieve the user's identity at any point without passing
        the JWT or request parameters manually:

        Automatic Cleanup: At the end of the request-response lifecycle,
        Spring Security automatically clears this thread's context so the user's
        details don't bleed into another request served by the same thread.
         */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info(
                "JWT authentication successful. username={}, method={}, path={}",
                username,
                request.getMethod(),
                request.getRequestURI()
        );

        filterChain.doFilter(request, response);

    }

}
