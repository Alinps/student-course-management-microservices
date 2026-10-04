package com.example.api_gateway.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;



    // =========================
    // EXTRACT CLAIMS
    // ========================
    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public String extractUsername(String token) {

        return extractClaims(token)
                .getSubject();
    }


    public Long extractUserID(String token) {
        return extractClaims(token)
                .get("userId",Long.class);
    }

    public List<String> extractRoles(String token) {
        return extractClaims(token)
                .get("roles", List.class);
    }

    // =========================
    // VALIDATE TOKEN
    // =========================
    public boolean isTokenValid(String token) {

        try {
            Claims claims  = extractClaims(token);
            return true;
        }  catch (Exception exception) {
            return false;
        }
    }

    // =========================
    // SIGNING KEY
    // =========================
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
