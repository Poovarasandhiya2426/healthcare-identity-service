package com.healthcare.identity.constants;

public final class SecurityConstants {

    private SecurityConstants() {
        // Prevent object creation
    }

    public static final String AUTHORIZATION_HEADER =
            "Authorization";

    public static final String BEARER_PREFIX =
            "Bearer ";

    public static final String JWT_CLAIM_USER_ID =
            "userId";

    public static final String JWT_CLAIM_ROLE =
            "role";

    public static final String TOKEN_TYPE_BEARER =
            "Bearer";

    public static final long ACCESS_TOKEN_EXPIRATION_SECONDS =
            900L;

    public static final long REFRESH_TOKEN_EXPIRATION_SECONDS =
            604800L;
}