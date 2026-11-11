package com.healthcare.identity.service;

import com.healthcare.identity.dto.request.UpdateUserRequest;
import com.healthcare.identity.dto.response.ApiResponse;
import com.healthcare.identity.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    ApiResponse<UserResponse> getUserById(Long userId);

    ApiResponse<UserResponse> getUserByEmail(String email);

    ApiResponse<List<UserResponse>> getAllUsers();

    ApiResponse<UserResponse> updateUser(
            Long userId,
            UpdateUserRequest request
    );

    ApiResponse<Void> deactivateUser(Long userId);
}