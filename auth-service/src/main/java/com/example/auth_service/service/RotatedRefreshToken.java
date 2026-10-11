package com.example.auth_service.service;

import com.example.auth_service.models.User;

/**
 * Carries the result of a successful refresh-token rotation.
 *
 * The User is used to generate a new access JWT.
 * The raw refresh token is returned to the client.
 */
public record RotatedRefreshToken(
        User user,
        String rawToken
) {
}