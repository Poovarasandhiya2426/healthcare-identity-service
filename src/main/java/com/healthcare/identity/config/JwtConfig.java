
package com.healthcare.identity.config;

import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfig {

    private String secret;

    private long accessTokenExpiration;

    private long refreshTokenExpiration;

    @PostConstruct
    public void validateConfiguration() {

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT secret is missing. Configure JWT_SECRET environment variable."
            );
        }

        final byte[] keyBytes;

        try {
            keyBytes = Decoders.BASE64URL.decode(secret);
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "JWT secret must be a valid Base64URL-encoded value.",
                    exception
            );
        }

        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT secret must contain at least 32 decoded bytes for HS256."
            );
        }

        if (accessTokenExpiration <= 0) {
            throw new IllegalStateException(
                    "JWT access token expiration must be greater than zero."
            );
        }

        if (refreshTokenExpiration <= 0) {
            throw new IllegalStateException(
                    "JWT refresh token expiration must be greater than zero."
            );
        }
    }
}
