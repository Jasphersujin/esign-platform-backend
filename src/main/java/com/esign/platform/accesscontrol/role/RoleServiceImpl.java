package com.esign.platform.accesscontrol.role;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.esign.platform.accesscontrol.role.dto.CreateRoleRequestDTO;
import com.esign.platform.accesscontrol.role.dto.RoleResponseDTO;
import com.esign.platform.accesscontrol.role.dto.UpdateRoleRequestDTO;
import com.esign.platform.common.exception.BusinessException;
import com.esign.platform.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponseDTO createRole(CreateRoleRequestDTO request) {

        String roleName = request.getRoleName().trim();

        /*
         * ==========================
         * GLOBAL ROLE VALIDATION
         * ==========================
         */
        if (request.getRoleType() == RoleType.GLOBAL) {

            if (request.getOrganizationId() != null) {
                throw new BusinessException(
                        "Global roles cannot belong to an organization.");
            }

            if (roleRepository.existsByRoleTypeAndRoleNameIgnoreCase(
                    RoleType.GLOBAL,
                    roleName)) {

                throw new BusinessException(
                        "Global role already exists.");
            }

        }

        /*
         * ==========================
         * TENANT ROLE VALIDATION
         * ==========================
         */
        else {

            if (request.getOrganizationId() == null) {
                throw new BusinessException(
                        "Organization is required for Tenant Role.");
            }

            /*
             * Global Role Name Reserved
             */
            if (roleRepository.existsByRoleTypeAndRoleNameIgnoreCase(
                    RoleType.GLOBAL,
                    roleName)) {

                throw new BusinessException(
                        "This Role Name is reserved by the platform. Please choose another Role Name.");
            }

            /*
             * Duplicate inside same Organization
             */
            if (roleRepository.existsByOrganizationIdAndRoleNameIgnoreCase(
                    request.getOrganizationId(),
                    roleName)) {

                throw new BusinessException(
                        "Role already exists in this Organization.");
            }

        }

        Role role = new Role();

        role.setOrganizationId(request.getOrganizationId());
        role.setRoleName(roleName);
        role.setDescription(request.getDescription());
        role.setRoleType(request.getRoleType());
        role.setSystemRole(request.getSystemRole());

        role = roleRepository.save(role);

        return mapToResponse(role);

    }

    @Override
    public RoleResponseDTO updateRole(
            UUID roleId,
            UpdateRoleRequestDTO request) {

        Role role = roleRepository.findByIdAndDeletedFalse(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        /*
         * System Roles cannot be modified
         */
        if (Boolean.TRUE.equals(role.getSystemRole())) {
            throw new BusinessException(
                    "System Roles cannot be modified.");
        }

        String roleName = request.getRoleName().trim();

        /*
         * GLOBAL ROLE
         */
        if (role.getRoleType() == RoleType.GLOBAL) {

            boolean exists = roleRepository
                    .existsByRoleTypeAndRoleNameIgnoreCase(
                            RoleType.GLOBAL,
                            roleName);

            if (exists &&
                    !role.getRoleName().equalsIgnoreCase(roleName)) {

                throw new BusinessException(
                        "Global Role already exists.");
            }

        }

        /*
         * TENANT ROLE
         */
        else {

            /*
             * Cannot use Global Role Name
             */
            if (roleRepository.existsByRoleTypeAndRoleNameIgnoreCase(
                    RoleType.GLOBAL,
                    roleName)) {

                throw new BusinessException(
                        "This Role Name is reserved by the platform.");
            }

            boolean exists = roleRepository
                    .existsByOrganizationIdAndRoleNameIgnoreCase(
                            role.getOrganizationId(),
                            roleName);

            if (exists &&
                    !role.getRoleName().equalsIgnoreCase(roleName)) {

                throw new BusinessException(
                        "Role already exists in this Organization.");
            }

        }

        role.setRoleName(roleName);
        role.setDescription(request.getDescription());

        if (request.getActive() != null) {
            role.setActive(request.getActive());
        }

        role = roleRepository.save(role);

        return mapToResponse(role);

    }
    
    @Override
    public RoleResponseDTO getRoleById(UUID roleId) {

        Role role = roleRepository.findByIdAndDeletedFalse(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        return mapToResponse(role);

    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {

        return roleRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<RoleResponseDTO> getGlobalRoles() {

        return roleRepository.findByRoleTypeAndDeletedFalse(RoleType.GLOBAL)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<RoleResponseDTO> getRolesByOrganization(UUID organizationId) {

        return roleRepository.findByOrganizationIdAndDeletedFalse(organizationId)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public void deleteRole(UUID roleId) {

        Role role = roleRepository.findByIdAndDeletedFalse(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        /*
         * System Roles cannot be deleted
         */
        if (Boolean.TRUE.equals(role.getSystemRole())) {
            throw new BusinessException(
                    "System Roles cannot be deleted.");
        }

        role.setDeleted(true);
        role.setActive(false);

        roleRepository.save(role);

    }

    /**
     * Entity -> Response DTO
     */
    private RoleResponseDTO mapToResponse(Role role) {

        return RoleResponseDTO.builder()
                .id(role.getId())
                .organizationId(role.getOrganizationId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .roleType(role.getRoleType())
                .systemRole(role.getSystemRole())
                .active(role.getActive())
                .deleted(role.getDeleted())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();

    }

}