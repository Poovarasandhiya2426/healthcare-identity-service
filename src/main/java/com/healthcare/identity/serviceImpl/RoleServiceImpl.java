package com.healthcare.identity.serviceImpl;

import com.healthcare.identity.dto.response.ApiResponse;
import com.healthcare.identity.entity.Role;
import com.healthcare.identity.enums.RoleName;
import com.healthcare.identity.repository.RoleRepository;
import com.healthcare.identity.service.RoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public ApiResponse<List<String>> getAllRoles() {

        List<String> roles = roleRepository.findAll()
                .stream()
                .map(role -> role.getName().name())
                .toList();

        return ApiResponse.<List<String>>builder()
                .success(true)
                .message("Roles retrieved successfully")
                .data(roles)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<String> getRoleByName(RoleName roleName) {

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new IllegalArgumentException("Role not found"));

        return ApiResponse.<String>builder()
                .success(true)
                .message("Role retrieved successfully")
                .data(role.getName().name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}