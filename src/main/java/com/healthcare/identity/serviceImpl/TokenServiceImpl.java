package com.healthcare.identity.serviceImpl;

import com.healthcare.identity.config.JwtConfig;
import com.healthcare.identity.entity.User;
import com.healthcare.identity.service.TokenService;
import com.healthcare.identity.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;

    @Override
    public String generateAccessToken(User user) {
        return jwtUtil.generateAccessToken(user);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    @Override
    public String extractEmail(String token) {
        return jwtUtil.extractEmail(token);
    }

    @Override
    public long getExpirationTime() {
        return jwtConfig.getAccessTokenExpiration() / 1000;
    }
}