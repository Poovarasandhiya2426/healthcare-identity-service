package com.healthcare.identity.serviceImpl;

import com.healthcare.identity.constants.IdentityConstants;
import com.healthcare.identity.dto.request.UpdateUserRequest;
import com.healthcare.identity.dto.response.ApiResponse;
import com.healthcare.identity.dto.response.UserResponse;
import com.healthcare.identity.entity.User;
import com.healthcare.identity.enums.AccountStatus;
import com.healthcare.identity.exception.ResourceNotFoundException;
import com.healthcare.identity.mapper.UserMapper;
import com.healthcare.identity.repository.UserRepository;
import com.healthcare.identity.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<UserResponse> getUserById(Long userId) {

        User user = findUserById(userId);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User details retrieved successfully")
                .data(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<UserResponse> getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User details retrieved successfully")
                .data(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();

        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(users)
                .build();
    }

    @Override
    public ApiResponse<UserResponse> updateUser(
            Long userId,
            UpdateUserRequest request) {

        User user = findUserById(userId);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User updatedUser = userRepository.save(user);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User updated successfully")
                .data(userMapper.toResponse(updatedUser))
                .build();
    }

    @Override
    public ApiResponse<Void> deactivateUser(Long userId) {

        User user = findUserById(userId);

        user.setAccountStatus(AccountStatus.INACTIVE);

        userRepository.save(user);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("User deactivated successfully")
                .data(null)
                .build();
    }

    private User findUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                IdentityConstants.USER_NOT_FOUND
                                        + " with ID: " + userId
                        )
                );
    }
}