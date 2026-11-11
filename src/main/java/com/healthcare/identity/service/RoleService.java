package com.healthcare.identity.service;

import com.healthcare.identity.dto.response.ApiResponse;
import com.healthcare.identity.dto.response.UserResponse;
import com.healthcare.identity.enums.RoleName;

import java.util.List;

public interface RoleService {

    ApiResponse<List<String>> getAllRoles();

    ApiResponse<String> getRoleByName(RoleName roleName);
}