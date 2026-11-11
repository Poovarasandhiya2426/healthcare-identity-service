package com.healthcare.identity.controller;

import com.healthcare.identity.dto.request.ChangePasswordRequest;
import com.healthcare.identity.dto.request.ForgotPasswordRequest;
import com.healthcare.identity.dto.request.ResetPasswordRequest;
import com.healthcare.identity.dto.response.ApiResponse;
import com.healthcare.identity.service.PasswordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PutMapping("/users/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(
                passwordService.changePassword(request)
        );
    }

    @PostMapping("/auth/password/forgot")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        return ResponseEntity.ok(
                passwordService.forgotPassword(request)
        );
    }

    @PostMapping("/auth/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        return ResponseEntity.ok(
                passwordService.resetPassword(request)
        );
    }
}