package com.example.employee_service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUser {

    private AuthenticatedUser() {
    }

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

}
