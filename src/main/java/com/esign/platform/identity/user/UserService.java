package com.esign.platform.identity.user;

import java.util.List;
import java.util.UUID;

import com.esign.platform.identity.user.dto.CreateUserDTO;
import com.esign.platform.identity.user.dto.LoginDTO;
import com.esign.platform.identity.user.dto.LoginResponseDTO;
import com.esign.platform.identity.user.dto.UpdateUserDTO;
import com.esign.platform.identity.user.dto.UserResponseDTO;

public interface UserService {

    /**
     * Create User
     */
    UserResponseDTO createUser(CreateUserDTO request);

    /**
     * Update User
     */
    UserResponseDTO updateUser(UUID userId, UpdateUserDTO request);

    /**
     * Get User By Id
     */
    UserResponseDTO getUserById(UUID userId);

    /**
     * Get All Users
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Get Users By Organization
     */
    List<UserResponseDTO> getUsersByOrganization(UUID organizationId);

    /**
     * Get Users By Department
     */
    List<UserResponseDTO> getUsersByDepartment(UUID departmentId);

    /**
     * Get Users By Role
     */
    List<UserResponseDTO> getUsersByRole(UUID roleId);

    /**
     * Login
     */
//    UserResponseDTO login(LoginDTO request);
    LoginResponseDTO login(LoginDTO request);

    /**
     * Delete User (Soft Delete)
     */
    void deleteUser(UUID userId);

    /**
     * Lock User
     */
    void lockUser(UUID userId);

    /**
     * Unlock User
     */
    void unlockUser(UUID userId);

    /**
     * Reset Password
     */
    void resetPassword(UUID userId, String newPassword);

    /**
     * Change Password
     */
    void changePassword(UUID userId, String oldPassword, String newPassword);

}