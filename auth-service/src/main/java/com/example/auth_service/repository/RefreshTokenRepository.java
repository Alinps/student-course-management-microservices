package com.example.auth_service.repository;

import com.example.auth_service.models.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds a refresh token by its SHA-256 hash.
     *
     * PESSIMISTIC_WRITE locks the matching database row until the
     * current transaction completes.
     *
     * This helps prevent two concurrent refresh requests from
     * successfully rotating the same token.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT token
            FROM RefreshToken token
            WHERE token.tokenHash = :tokenHash
            """)
    Optional<RefreshToken> findByTokenHashForUpdate(
            @Param("tokenHash") String tokenHash
    );

    /**
     * Finds all refresh tokens associated with a token family.
     *
     * Used when revoking a family after detecting refresh-token reuse.
     */
    List<RefreshToken> findAllByFamilyId(UUID familyId);
}