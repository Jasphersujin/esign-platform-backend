package com.esign.platform.identity.user;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.esign.platform.common.dto.ApiResponse;
import com.esign.platform.identity.user.dto.CreateUserDTO;
import com.esign.platform.identity.user.dto.LoginDTO;
import com.esign.platform.identity.user.dto.LoginResponseDTO;
import com.esign.platform.identity.user.dto.UpdateUserDTO;
import com.esign.platform.identity.user.dto.UserResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * Create User
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody CreateUserDTO request) {

        UserResponseDTO response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponseDTO>builder()
                        .success(true)
                        .message("User created successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Update User
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserDTO request) {

        UserResponseDTO response = userService.updateUser(userId, request);

        return ResponseEntity.ok(
                ApiResponse.<UserResponseDTO>builder()
                        .success(true)
                        .message("User updated successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Get User By Id
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @PathVariable UUID userId) {

        UserResponseDTO response = userService.getUserById(userId);

        return ResponseEntity.ok(
                ApiResponse.<UserResponseDTO>builder()
                        .success(true)
                        .message("User fetched successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Get All Users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {

        List<UserResponseDTO> response = userService.getAllUsers();

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponseDTO>>builder()
                        .success(true)
                        .message("Users fetched successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Get Users By Organization
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsersByOrganization(
            @PathVariable UUID organizationId) {

        List<UserResponseDTO> response =
                userService.getUsersByOrganization(organizationId);

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponseDTO>>builder()
                        .success(true)
                        .message("Organization users fetched successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Get Users By Department
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsersByDepartment(
            @PathVariable UUID departmentId) {

        List<UserResponseDTO> response =
                userService.getUsersByDepartment(departmentId);

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponseDTO>>builder()
                        .success(true)
                        .message("Department users fetched successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Get Users By Role
     */
    @GetMapping("/role/{roleId}")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsersByRole(
            @PathVariable UUID roleId) {

        List<UserResponseDTO> response =
                userService.getUsersByRole(roleId);

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponseDTO>>builder()
                        .success(true)
                        .message("Role users fetched successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Login
     */
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
//            @Valid @RequestBody LoginDTO request) {
//
//        UserResponseDTO response = userService.login(request);
//
//        return ResponseEntity.ok(
//                ApiResponse.<UserResponseDTO>builder()
//                        .success(true)
//                        .message("Login successful.")
//                        .data(response)
//                        .build());
//    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginDTO request) {

        LoginResponseDTO response =
                userService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponseDTO>builder()
                        .success(true)
                        .message("Login successful.")
                        .data(response)
                        .build());
    }

    /**
     * Lock User
     */
    @PatchMapping("/{userId}/lock")
    public ResponseEntity<ApiResponse<Object>> lockUser(
            @PathVariable UUID userId) {

        userService.lockUser(userId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("User locked successfully.")
                        .build());
    }

    /**
     * Unlock User
     */
    @PatchMapping("/{userId}/unlock")
    public ResponseEntity<ApiResponse<Object>> unlockUser(
            @PathVariable UUID userId) {

        userService.unlockUser(userId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("User unlocked successfully.")
                        .build());
    }

    /**
     * Reset Password
     */
    @PatchMapping("/{userId}/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(
            @PathVariable UUID userId,
            @RequestParam String newPassword) {

        userService.resetPassword(userId, newPassword);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Password reset successfully.")
                        .build());
    }

    /**
     * Change Password
     */
    @PatchMapping("/{userId}/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @PathVariable UUID userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {

        userService.changePassword(userId, oldPassword, newPassword);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Password changed successfully.")
                        .build());
    }

    /**
     * Delete User (Soft Delete)
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(
            @PathVariable UUID userId) {

        userService.deleteUser(userId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("User deleted successfully.")
                        .build());
    }

}