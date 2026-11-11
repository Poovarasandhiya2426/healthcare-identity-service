package com.healthcare.identity.serviceImpl;

import com.healthcare.identity.dto.request.*;
import com.healthcare.identity.dto.response.ApiResponse;
import com.healthcare.identity.entity.PasswordResetToken;
import com.healthcare.identity.entity.User;
import com.healthcare.identity.repository.PasswordResetTokenRepository;
import com.healthcare.identity.repository.UserRepository;
import com.healthcare.identity.service.PasswordService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public ApiResponse<Void> changePassword(ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<Void> forgotPassword(ForgotPasswordRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {

                    byte[] randomBytes = new byte[32];
                    secureRandom.nextBytes(randomBytes);

                    String token = Base64.getUrlEncoder()
                            .withoutPadding()
                            .encodeToString(randomBytes);

                    PasswordResetToken resetToken =
                            PasswordResetToken.builder()
                                    .token(token)
                                    .user(user)
                                    .expiryDate(
                                            LocalDateTime.now().plusMinutes(15))
                                    .used(false)
                                    .build();

                    passwordResetTokenRepository.save(resetToken);
                });

        return ApiResponse.<Void>builder()
                .success(true)
                .message("If the email is registered, password reset instructions will be sent")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<Void> resetPassword(ResetPasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(request.getResetToken())
                        .orElseThrow(() ->
                                new IllegalArgumentException("Invalid reset token"));

        if (resetToken.isUsed()) {
            throw new IllegalArgumentException("Reset token already used");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        resetToken.setUsed(true);

        passwordResetTokenRepository.save(resetToken);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }
}