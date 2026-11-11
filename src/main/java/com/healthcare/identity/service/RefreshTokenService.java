package com.healthcare.identity.service;

import com.healthcare.identity.entity.RefreshToken;
import com.healthcare.identity.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void revokeRefreshToken(String token);

    void revokeAllUserTokens(User user);
}