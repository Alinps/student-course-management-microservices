CREATE TABLE refresh_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NOT NULL,

    token_hash VARCHAR(64) NOT NULL,

    family_id BINARY(16) NOT NULL,

    expires_at DATETIME(6) NOT NULL,

    revoked_at DATETIME(6) NULL,

    replaced_by_token_id BIGINT NULL,

    created_at DATETIME(6) NOT NULL,

    updated_at DATETIME(6) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_refresh_tokens_token_hash
        UNIQUE (token_hash),

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_refresh_tokens_replaced_by
        FOREIGN KEY (replaced_by_token_id)
        REFERENCES refresh_tokens(id),

    INDEX idx_refresh_token_user_id (user_id),

    INDEX idx_refresh_token_family_id (family_id)
);
