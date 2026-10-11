package com.example.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request body for refreshing an access token.
 *
 * The client submits the refresh token received during login.
 * The server validates it before issuing a replacement token pair.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    /**
     * The opaque refresh token previously issued by the Auth Service.
     *
     * It must not be null, empty, or whitespace-only.
     */
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}