package com.healthcare.identity.serviceImpl;

import com.healthcare.identity.constants.IdentityConstants;
import com.healthcare.identity.dto.request.LoginRequest;
import com.healthcare.identity.dto.request.RefreshTokenRequest;
import com.healthcare.identity.dto.request.RegisterRequest;
import com.healthcare.identity.dto.response.ApiResponse;
import com.healthcare.identity.dto.response.AuthResponse;
import com.healthcare.identity.entity.RefreshToken;
import com.healthcare.identity.entity.Role;
import com.healthcare.identity.entity.User;
import com.healthcare.identity.enums.AccountStatus;
import com.healthcare.identity.enums.RoleName;
import com.healthcare.identity.exception.EmailAlreadyExistsException;
import com.healthcare.identity.exception.InvalidTokenException;
import com.healthcare.identity.mapper.UserMapper;
import com.healthcare.identity.repository.RoleRepository;
import com.healthcare.identity.repository.UserRepository;
import com.healthcare.identity.service.AuthService;
import com.healthcare.identity.service.RefreshTokenService;
import com.healthcare.identity.service.TokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Override
    public ApiResponse<AuthResponse> register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    IdentityConstants.EMAIL_ALREADY_EXISTS
            );
        }

        Role patientRole = roleRepository.findByName(RoleName.ROLE_PATIENT)
                .orElseThrow(() ->
                        new IllegalStateException("Patient role not found")
                );

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(patientRole)
                .accountStatus(AccountStatus.ACTIVE)
                .emailVerified(true)
                .build();

        User savedUser = userRepository.save(user);

        return buildAuthResponse(
                savedUser,
                IdentityConstants.USER_REGISTERED
        );
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidTokenException(
                                IdentityConstants.INVALID_CREDENTIALS
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidTokenException(
                    IdentityConstants.INVALID_CREDENTIALS
            );
        }

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("User account is not active");
        }

        return buildAuthResponse(
                user,
                IdentityConstants.LOGIN_SUCCESS
        );
    }

    @Override
    public ApiResponse<AuthResponse> refreshToken(
            RefreshTokenRequest request) {

        RefreshToken oldToken =
                refreshTokenService.verifyRefreshToken(
                        request.getRefreshToken()
                );

        User user = oldToken.getUser();

        refreshTokenService.revokeRefreshToken(
                request.getRefreshToken()
        );

        return buildAuthResponse(
                user,
                "Token refreshed successfully"
        );
    }

    @Override
    public ApiResponse<Void> logout(String refreshToken) {

        refreshTokenService.revokeRefreshToken(refreshToken);

        return ApiResponse.<Void>builder()
                .success(true)
                .message(IdentityConstants.LOGOUT_SUCCESS)
                .data(null)
                .build();
    }

    private ApiResponse<AuthResponse> buildAuthResponse(
            User user,
            String message) {

        String accessToken =
                tokenService.generateAccessToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(tokenService.getExpirationTime())
                .user(userMapper.toResponse(user))
                .build();

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message(message)
                .data(authResponse)
                .build();
    }
}