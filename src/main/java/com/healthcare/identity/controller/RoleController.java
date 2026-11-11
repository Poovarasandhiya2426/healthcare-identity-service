package com.healthcare.identity.controller;

import com.healthcare.identity.dto.response.ApiResponse;
import com.healthcare.identity.enums.RoleName;
import com.healthcare.identity.service.RoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> getAllRoles() {

        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{roleName}")
    public ResponseEntity<ApiResponse<String>> getRoleByName(
            @PathVariable RoleName roleName) {

        return ResponseEntity.ok(
                roleService.getRoleByName(roleName)
        );
    }
}