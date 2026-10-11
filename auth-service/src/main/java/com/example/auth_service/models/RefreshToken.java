package com.example.auth_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_refresh_tokens_token_hash",
                        columnNames = "token_hash"
                )
        },
        indexes = {
                @Index(
                        name = "idx_refresh_token_user_id",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_refresh_token_family_id",
                        columnList = "family_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(
                name = "user_id",
                nullable = false,
                foreignKey = @ForeignKey(
                        name = "fk_refresh_tokens_user"
                )
        )
        private User user;

        @Column(
                name = "token_hash",
                nullable = false,
                length = 64
        )
        private String tokenHash;

        @JdbcTypeCode(SqlTypes.BINARY)
        @Column(
                name = "family_id",
                nullable = false,
                updatable = false,
                columnDefinition = "BINARY(16)"
        )
        private UUID familyId;

        @Column(
                name = "expires_at",
                nullable = false,
                columnDefinition = "DATETIME(6)"
        )
        private LocalDateTime expiresAt;

        @Column(
                name = "revoked_at",
                columnDefinition = "DATETIME(6)"
        )
        private LocalDateTime revokedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(
                name = "replaced_by_token_id",
                foreignKey = @ForeignKey(
                        name = "fk_refresh_tokens_replaced_by"
                )
        )
        private RefreshToken replacedByToken;

        public boolean isRevoked() {
                return revokedAt != null;
        }

        public boolean isExpired() {
                return !expiresAt.isAfter(
                        LocalDateTime.now(ZoneOffset.UTC)
                );
        }

        public boolean isUsable() {
                return !isRevoked() && !isExpired();
        }
}