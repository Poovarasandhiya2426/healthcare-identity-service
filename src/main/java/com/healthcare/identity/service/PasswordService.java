package com.healthcare.identity.service;

import com.healthcare.identity.dto.request.ChangePasswordRequest;
import com.healthcare.identity.dto.request.ForgotPasswordRequest;
import com.healthcare.identity.dto.request.ResetPasswordRequest;
import com.healthcare.identity.dto.response.ApiResponse;

public interface PasswordService {

    ApiResponse<Void> changePassword(ChangePasswordRequest request);

    ApiResponse<Void> forgotPassword(ForgotPasswordRequest request);

    ApiResponse<Void> resetPassword(ResetPasswordRequest request);
}