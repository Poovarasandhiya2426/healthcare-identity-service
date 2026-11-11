package com.healthcare.identity.service;

import com.healthcare.identity.dto.request.LoginRequest;
import com.healthcare.identity.dto.request.RegisterRequest;
import com.healthcare.identity.dto.request.RefreshTokenRequest;
import com.healthcare.identity.dto.response.AuthResponse;
import com.healthcare.identity.dto.response.ApiResponse;

public interface AuthService {

    ApiResponse<AuthResponse> register(RegisterRequest request);

    ApiResponse<AuthResponse> login(LoginRequest request);

    ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request);

    ApiResponse<Void> logout(String refreshToken);
}