package com.healthcare.identity.mapper;

import com.healthcare.identity.dto.response.UserResponse;
import com.healthcare.identity.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .accountStatus(user.getAccountStatus().name())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}