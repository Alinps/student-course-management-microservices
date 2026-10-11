package com.example.auth_service.service;

import com.example.auth_service.models.RefreshToken;
import com.example.auth_service.models.User;
import com.example.auth_service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_service.exception.InvalidCredentialException;
import java.time.Duration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;
import java.time.ZoneOffset;

/**
 * Service responsible for creating and securely storing refresh tokens.
 *
 * A refresh token allows a client to request a new access token after the
 * current access token expires, without requiring the user to log in again.
 *
 * Unlike an access token, which is a JWT containing claims, this refresh
 * token is an opaque random string. Its contents do not carry user details.
 *
 * Security approach:
 * 1. Generate a cryptographically secure random token.
 * 2. Calculate the SHA-256 hash of the token.
 * 3. Store only the hash in the database.
 * 4. Return the original token to the client.
 *
 * Storing only the hash reduces the risk of exposing usable refresh tokens
 * if someone obtains a copy of the database.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    /**
     * Repository used to save and retrieve refresh-token records
     * from the database.
     *
     * Spring Data JPA provides the implementation automatically.
     */
    private final RefreshTokenRepository refreshTokenRepository;


    /**
     * SecureRandom generates cryptographically strong random values.
     *
     * These random bytes are used to create refresh tokens that are
     * difficult for an attacker to predict or guess.
     *
     * SecureRandom is suitable for security-sensitive random values,
     * unlike the ordinary java.util.Random class.
     */
    private final SecureRandom secureRandom = new SecureRandom();


    /**
     * Lifetime of a refresh token, expressed in milliseconds.
     *
     * Spring injects this value from application.properties.
     *
     * Example:
     * app.refresh-token.expiration=604800000
     *
     * 604800000 milliseconds = 7 days.
     */
    @Value("${app.refresh-token.expiration}")
    private long refreshTokenExpiration;


    /**
     * Creates a new refresh token for the specified user.
     *
     * This method performs the following operations:
     *
     * 1. Generates 32 cryptographically secure random bytes.
     * 2. Encodes those bytes as a URL-safe string.
     * 3. Calculates the SHA-256 hash of the token.
     * 4. Creates a RefreshToken entity.
     * 5. Associates the token record with the user.
     * 6. Assigns a token-family identifier.
     * 7. Calculates the token's expiration date and time.
     * 8. Saves the token record in the database.
     * 9. Returns the original token to the caller.
     *
     * The raw token is never stored in the database.
     *
     * @param user the user for whom the refresh token is created
     * @return the raw refresh token that can be sent to the client
     */
    @Transactional
    public String createRefreshToken(User user) {

        /*
         * STEP 1: Generate cryptographically secure random bytes.
         *
         * A length of 32 bytes provides 256 bits of randomness.
         *
         * The token is not derived from the user's ID, username,
         * password, or any other predictable information.
         */
        byte[] randomBytes = new byte[32];

        secureRandom.nextBytes(randomBytes);


        /*
         * STEP 2: Convert the random bytes into a string.
         *
         * Base64 URL encoding converts binary data into a string that
         * can safely be transmitted in URLs and HTTP requests.
         *
         * withoutPadding() removes the optional trailing '=' characters.
         *
         * This operation encodes the bytes; it does not encrypt them.
         */
        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);


        /*
         * STEP 3: Hash the raw refresh token.
         *
         * SHA-256 produces a 256-bit hash, represented here as
         * a 64-character hexadecimal string.
         *
         * For example:
         * Raw token  -> randomly generated secret
         * Token hash -> SHA-256 hash of that secret
         *
         * The database stores the hash rather than the raw token.
         *
         * Later, when a client submits a refresh token, the application
         * can hash the submitted value and compare it with the stored hash.
         */
        String tokenHash = hashToken(rawToken);


        /*
         * STEP 4: Create a new RefreshToken entity.
         *
         * This Java object represents a row in the refresh_tokens table.
         *
         * At this stage, the object exists in application memory.
         * It will be persisted when the repository saves it.
         */
        RefreshToken refreshToken = new RefreshToken();


        /*
         * STEP 5: Associate the refresh token with its owner.
         *
         * The RefreshToken entity has a relationship with the User entity.
         *
         * This association allows the application to identify which user
         * owns a particular refresh token.
         *
         * The corresponding user_id column stores the database relationship.
         */
        refreshToken.setUser(user);


        /*
         * STEP 6: Store the token hash.
         *
         * Only the SHA-256 hash is stored.
         *
         * The raw token remains available in this method so that it can
         * be returned to the client, but it is not assigned to the entity.
         */
        refreshToken.setTokenHash(tokenHash);


        /*
         * STEP 7: Generate a token-family identifier.
         *
         * A family identifies a group of refresh tokens connected through
         * token rotation.
         *
         * For example:
         *
         * Login:
         *     Token A -> Family X
         *
         * First refresh:
         *     Token A is revoked.
         *     Token B is issued -> Family X
         *
         * Second refresh:
         *     Token B is revoked.
         *     Token C is issued -> Family X
         *
         * Keeping the same family ID across rotated tokens allows the
         * application to revoke the family if token reuse is detected.
         *
         * IMPORTANT:
         * This method currently creates a new family for each newly
         * created token. A future rotation method must explicitly reuse
         * the existing token's family ID instead of generating another one.
         */
        refreshToken.setFamilyId(UUID.randomUUID());


        /*
         * STEP 8: Calculate the expiration date and time.
         *
         * LocalDateTime.now() gives the current local date and time.
         *
         * refreshTokenExpiration is expressed in milliseconds.
         *
         * Therefore:
         *     1 millisecond = 1,000,000 nanoseconds
         *
         * Multiplying the configured value by 1,000,000 converts
         * milliseconds into nanoseconds for plusNanos().
         *
         * Example:
         * If the configured lifetime is seven days, the expiration
         * date and time will be seven days after the current time.
         */
        refreshToken.setExpiresAt(
                LocalDateTime.now(ZoneOffset.UTC)
                        .plus(Duration.ofMillis(refreshTokenExpiration))
        );


        /*
         * STEP 9: Save the refresh-token record.
         *
         * Spring Data JPA persists the entity in the database.
         *
         * The resulting row contains information such as:
         * - The token record ID
         * - The associated user ID
         * - The token hash
         * - The token-family ID
         * - The expiration timestamp
         * - Revocation information, initially null
         *
         * Because this method is transactional, the database operation
         * participates in a transaction.
         */
        refreshTokenRepository.save(refreshToken);


        /*
         * STEP 10: Return the raw refresh token.
         *
         * The client needs the original token to request new access tokens.
         *
         * The original token is returned only after the record has been
         * saved successfully.
         *
         * The caller can include this value in the login response.
         *
         * Do not log this value or store it in plaintext in the database.
         */
        return rawToken;
    }


    /**
     * Calculates the SHA-256 hash of a refresh token.
     *
     * The same input always produces the same hash, allowing the
     * application to look up a stored token record using a submitted token.
     *
     * @param token the raw refresh token
     * @return the SHA-256 hash encoded as a hexadecimal string
     */
    public String hashToken(String token) {

        try {

            /*
             * Obtain a MessageDigest implementation for SHA-256.
             *
             * MessageDigest is a Java API for calculating cryptographic
             * message digests, also known as hashes.
             */
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");


            /*
             * Convert the token string into bytes using UTF-8 encoding.
             *
             * Cryptographic hash algorithms operate on bytes, not directly
             * on Java String objects.
             *
             * digest() calculates the SHA-256 hash of those bytes.
             */
            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );


            /*
             * Convert the resulting hash bytes into hexadecimal text.
             *
             * SHA-256 produces 32 bytes.
             * Each byte is represented by two hexadecimal characters.
             * Therefore, the resulting hexadecimal string contains 64
             * characters, which fits the token_hash VARCHAR(64) column.
             */
            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {

            /*
             * SHA-256 is a standard algorithm supported by Java.
             *
             * If the Java runtime unexpectedly cannot provide it,
             * token creation cannot safely continue.
             *
             * Throw an unchecked exception so the operation fails rather
             * than proceeding without hashing the token.
             */
            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable",
                    exception
            );
        }
    }


    /**
     * Validates and rotates a refresh token.
     *
     * A successful rotation:
     * 1. Hashes the submitted token.
     * 2. Locks and finds its database record.
     * 3. Rejects unknown or expired tokens.
     * 4. Detects reuse of an already-revoked token.
     * 5. Revokes the existing token.
     * 6. Creates a replacement in the same token family.
     * 7. Links the old token to its replacement.
     *
     * noRollbackFor is important here: if a revoked token is reused,
     * the family is revoked before InvalidCredentialException is thrown.
     * We need that revocation to commit rather than roll back.
     */
    @Transactional(noRollbackFor = InvalidCredentialException.class)
    public RotatedRefreshToken rotateRefreshToken(String rawToken) {

        // Never search the database using the raw token.
        String tokenHash = hashToken(rawToken);

        // Lock the matching row so concurrent requests cannot both
        // rotate the same token successfully.
        RefreshToken currentToken = refreshTokenRepository
                .findByTokenHashForUpdate(tokenHash)
                .orElseThrow(() ->
                        new InvalidCredentialException(
                                "Invalid refresh token"
                        )
                );

        /*
         * A revoked token should never be accepted again.
         *
         * Reuse can indicate that an old token has been stolen.
         * Revoke every still-active token belonging to this family.
         *
         * This branch throws an exception, but noRollbackFor ensures
         * the family revocations are committed.
         */
        if (currentToken.isRevoked()) {
            revokeTokenFamily(currentToken.getFamilyId());

            throw new InvalidCredentialException(
                    "Refresh token reuse detected. Please log in again."
            );
        }

        // An expired token cannot be used to obtain another token pair.
        if (currentToken.isExpired()) {
            throw new InvalidCredentialException(
                    "Refresh token has expired. Please log in again."
            );
        }

        /*
         * Revoke the current token before creating its replacement.
         * The operation occurs in the same database transaction.
         */
        currentToken.setRevokedAt(
                LocalDateTime.now(ZoneOffset.UTC)
        );

        /*
         * Generate a new random token and its hash.
         * Keep the original family ID so the token rotation chain
         * remains associated with the same family.
         */
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String replacementRawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken replacement = new RefreshToken();

        replacement.setUser(currentToken.getUser());
        replacement.setTokenHash(hashToken(replacementRawToken));
        replacement.setFamilyId(currentToken.getFamilyId());
        replacement.setExpiresAt(
                LocalDateTime.now(ZoneOffset.UTC)
                        .plus(Duration.ofMillis(refreshTokenExpiration))
        );

        // Save the replacement so that it receives its database ID.
        RefreshToken savedReplacement = refreshTokenRepository.save(replacement);

        // Record the replacement token using the entity relationship.
        // This establishes a foreign-key relationship between the old
        // refresh-token record and its replacement.
        currentToken.setReplacedByToken(savedReplacement);

        // Persist the revocation and replacement link.
        refreshTokenRepository.save(currentToken);

        // Return the user and raw replacement token to the Auth Service.
        return new RotatedRefreshToken(
                currentToken.getUser(),
                replacementRawToken
        );
    }


    /**
     * Revokes every active refresh token in the specified family.
     *
     * This is used when a previously revoked token is submitted again.
     * Already-revoked tokens retain their original revocation timestamp.
     */
    private void revokeTokenFamily(UUID familyId) {

        LocalDateTime revokedAt = LocalDateTime.now(ZoneOffset.UTC);

        for (RefreshToken token :
                refreshTokenRepository.findAllByFamilyId(familyId)) {

            if (!token.isRevoked()) {
                token.setRevokedAt(revokedAt);
                refreshTokenRepository.save(token);
            }
        }
    }


    /**
     * Revokes a refresh token during logout.
     *
     * If the token exists and has not already been revoked, its
     * revokedAt timestamp is updated.
     *
     * If the token is unknown or already revoked, no action is taken.
     * This makes logout idempotent: repeating the same logout request
     * does not produce an error.
     *
     * This method revokes only the submitted token. It does not revoke
     * every token belonging to the same family.
     *
     * @param rawToken the refresh token submitted by the client
     */
    @Transactional
    public void revokeRefreshToken(String rawToken) {

        // Hash the submitted token before looking it up in the database.
        String tokenHash = hashToken(rawToken);

        /*
         * Find and lock the corresponding database row.
         *
         * The lock helps coordinate logout with other operations
         * attempting to modify the same refresh-token record.
         *
         * If the token does not exist, the Optional is empty and
         * the lambda is not executed.
         */
        refreshTokenRepository.findByTokenHashForUpdate(tokenHash)
                .ifPresent(token -> {

                    /*
                     * Revoke the token only if it has not already
                     * been revoked.
                     */
                    if (!token.isRevoked()) {

                        // Record the revocation time in UTC.
                        token.setRevokedAt(
                                LocalDateTime.now(ZoneOffset.UTC)
                        );

                        // Persist the updated token record.
                        refreshTokenRepository.save(token);
                    }
                });
    }


}