package com.healthcare.identity.service;

import com.healthcare.identity.entity.User;

public interface TokenService {

    String generateAccessToken(User user);

    boolean validateToken(String token);

    String extractEmail(String token);

    long getExpirationTime();
}