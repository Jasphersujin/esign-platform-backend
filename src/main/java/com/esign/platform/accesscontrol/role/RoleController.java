package com.esign.platform.accesscontrol.role;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.esign.platform.accesscontrol.role.dto.CreateRoleRequestDTO;
import com.esign.platform.accesscontrol.role.dto.RoleResponseDTO;
import com.esign.platform.accesscontrol.role.dto.UpdateRoleRequestDTO;
import com.esign.platform.common.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    /**
     * Create Role
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(
            @Valid @RequestBody CreateRoleRequestDTO request) {

        RoleResponseDTO response = roleService.createRole(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role created successfully.")
                        .data(response)
                        .build());

    }

    /**
     * Update Role
     */
    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody UpdateRoleRequestDTO request) {

        RoleResponseDTO response =
                roleService.updateRole(roleId, request);

        return ResponseEntity.ok(
                ApiResponse.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role updated successfully.")
                        .data(response)
                        .build());

    }

    /**
     * Get Role By Id
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getRoleById(
            @PathVariable UUID roleId) {

        RoleResponseDTO response =
                roleService.getRoleById(roleId);

        return ResponseEntity.ok(
                ApiResponse.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role fetched successfully.")
                        .data(response)
                        .build());

    }

    /**
     * Get All Roles
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getAllRoles() {

        List<RoleResponseDTO> response =
                roleService.getAllRoles();

        return ResponseEntity.ok(
                ApiResponse.<List<RoleResponseDTO>>builder()
                        .success(true)
                        .message("Roles fetched successfully.")
                        .data(response)
                        .build());

    }

    /**
     * Get Global Roles
     */
    @GetMapping("/global")
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getGlobalRoles() {

        List<RoleResponseDTO> response =
                roleService.getGlobalRoles();

        return ResponseEntity.ok(
                ApiResponse.<List<RoleResponseDTO>>builder()
                        .success(true)
                        .message("Global Roles fetched successfully.")
                        .data(response)
                        .build());

    }

    /**
     * Get Roles By Organization
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getRolesByOrganization(
            @PathVariable UUID organizationId) {

        List<RoleResponseDTO> response =
                roleService.getRolesByOrganization(organizationId);

        return ResponseEntity.ok(
                ApiResponse.<List<RoleResponseDTO>>builder()
                        .success(true)
                        .message("Organization Roles fetched successfully.")
                        .data(response)
                        .build());

    }

    /**
     * Delete Role
     */
    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Object>> deleteRole(
            @PathVariable UUID roleId) {

        roleService.deleteRole(roleId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Role deleted successfully.")
                        .build());

    }

}