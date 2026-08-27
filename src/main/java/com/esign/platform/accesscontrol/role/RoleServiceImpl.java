package com.esign.platform.accesscontrol.role;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esign.platform.accesscontrol.role.dto.CreateRoleRequestDTO;
import com.esign.platform.accesscontrol.role.dto.RoleResponseDTO;
import com.esign.platform.accesscontrol.role.dto.UpdateRoleRequestDTO;
import com.esign.platform.common.exception.BusinessException;
import com.esign.platform.common.exception.ResourceNotFoundException;
import com.esign.platform.organizationmanagement.organization.entity.OrganizationEntity;
import com.esign.platform.organizationmanagement.organization.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final OrganizationRepository organizationRepository;

    /*
     * ==========================================================
     * CREATE ROLE
     * ==========================================================
     */

    @Override
    public RoleResponseDTO createRole(
            CreateRoleRequestDTO request) {

        String roleName =
                request.getRoleName().trim();

        RoleType roleType =
                request.getRoleType();

        OrganizationEntity organization = null;

        /*
         * ======================================================
         * GLOBAL ROLE
         * ======================================================
         */

        if (roleType == RoleType.GLOBAL) {

            /*
             * Global role cannot have organization
             */

            if (request.getOrganizationId() != null) {

                throw new BusinessException(
                        "Global roles cannot belong to an organization.");
            }

            /*
             * Check duplicate global role
             */

            if (roleRepository
                    .existsByRoleTypeAndRoleNameIgnoreCase(
                            RoleType.GLOBAL,
                            roleName)) {

                throw new BusinessException(
                        "Global role already exists.");
            }
        }

        /*
         * ======================================================
         * TENANT ROLE
         * ======================================================
         */

        else if (roleType == RoleType.TENANT) {

            /*
             * Organization is mandatory
             */

            if (request.getOrganizationId() == null) {

                throw new BusinessException(
                        "Organization is required for Tenant Role.");
            }

            /*
             * Fetch organization
             */

            organization =
                    organizationRepository
                            .findById(
                                    request.getOrganizationId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Organization not found."));

            /*
             * Tenant cannot use a global role name
             */

            if (roleRepository
                    .existsByRoleTypeAndRoleNameIgnoreCase(
                            RoleType.GLOBAL,
                            roleName)) {

                throw new BusinessException(
                        "This Role Name is reserved by the platform. " +
                        "Please choose another Role Name.");
            }

            /*
             * Duplicate role inside same organization
             */

            if (roleRepository
                    .existsByOrganization_IdAndRoleNameIgnoreCase(
                            request.getOrganizationId(),
                            roleName)) {

                throw new BusinessException(
                        "Role already exists in this Organization.");
            }
        }

        /*
         * ======================================================
         * CREATE ENTITY
         * ======================================================
         */

        Role role = new Role();

        role.setOrganization(organization);

        role.setRoleName(roleName);

        role.setRoleCode(
                generateRoleCode(roleName));

        role.setDescription(
                request.getDescription());

        role.setRoleType(roleType);

        role.setSystemRole(
                Boolean.TRUE.equals(
                        request.getSystemRole()));

        role = roleRepository.save(role);

        return mapToResponse(role);
    }

    /*
     * ==========================================================
     * UPDATE ROLE
     * ==========================================================
     */

    @Override
    public RoleResponseDTO updateRole(
            UUID roleId,
            UpdateRoleRequestDTO request) {

        Role role =
                roleRepository
                        .findByIdAndDeletedFalse(roleId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Role not found."));

        /*
         * ======================================================
         * SYSTEM ROLE PROTECTION
         * ======================================================
         *
         * Existing system roles cannot be modified.
         */

        if (Boolean.TRUE.equals(
                role.getSystemRole())) {

            throw new BusinessException(
                    "System Roles cannot be modified.");
        }

        String roleName =
                request.getRoleName().trim();

        RoleType requestedRoleType =
                request.getRoleType();

        OrganizationEntity organization = null;

        /*
         * ======================================================
         * GLOBAL ROLE
         * ======================================================
         */

        if (requestedRoleType == RoleType.GLOBAL) {

            /*
             * Global role cannot have organization
             */

            if (request.getOrganizationId() != null) {

                throw new BusinessException(
                        "Global roles cannot belong to an organization.");
            }

            /*
             * Check duplicate global role
             *
             * Exclude current role from the check.
             */

            boolean exists =
                    roleRepository
                            .existsByRoleTypeAndRoleNameIgnoreCaseAndIdNot(
                                    RoleType.GLOBAL,
                                    roleName,
                                    roleId);

            if (exists) {

                throw new BusinessException(
                        "Global Role already exists.");
            }
        }

        /*
         * ======================================================
         * TENANT ROLE
         * ======================================================
         */

        else if (requestedRoleType == RoleType.TENANT) {

            /*
             * Organization is mandatory
             */

            if (request.getOrganizationId() == null) {

                throw new BusinessException(
                        "Organization is required for Tenant Role.");
            }

            /*
             * Fetch organization
             */

            organization =
                    organizationRepository
                            .findById(
                                    request.getOrganizationId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Organization not found."));

            /*
             * Tenant role cannot use global role name
             */

            if (roleRepository
                    .existsByRoleTypeAndRoleNameIgnoreCase(
                            RoleType.GLOBAL,
                            roleName)) {

                throw new BusinessException(
                        "This Role Name is reserved by the platform.");
            }

            /*
             * Check duplicate within organization.
             *
             * Exclude current role.
             */

            boolean exists =
                    roleRepository
                            .existsByOrganization_IdAndRoleNameIgnoreCaseAndIdNot(
                                    request.getOrganizationId(),
                                    roleName,
                                    roleId);

            if (exists) {

                throw new BusinessException(
                        "Role already exists in this Organization.");
            }
        }

        /*
         * ======================================================
         * UPDATE ENTITY
         * ======================================================
         */

        role.setOrganization(organization);

        role.setRoleName(roleName);

        /*
         * Regenerate role code if role name changes.
         */

        role.setRoleCode(
                generateRoleCode(roleName));

        role.setDescription(
                request.getDescription());

        role.setRoleType(
                requestedRoleType);

        /*
         * System role
         */

        if (request.getSystemRole() != null) {

            role.setSystemRole(
                    request.getSystemRole());
        }

        /*
         * Active status
         */

        if (request.getActive() != null) {

            role.setActive(
                    request.getActive());
        }

        role = roleRepository.save(role);

        return mapToResponse(role);
    }

    /*
     * ==========================================================
     * GET ROLE BY ID
     * ==========================================================
     */

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(
            UUID roleId) {

        Role role =
                roleRepository
                        .findByIdAndDeletedFalse(roleId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Role not found."));

        return mapToResponse(role);
    }

    /*
     * ==========================================================
     * GET ALL ROLES
     * ==========================================================
     */

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAllRoles() {

        return roleRepository
                .findByDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * ==========================================================
     * GET GLOBAL ROLES
     * ==========================================================
     */

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getGlobalRoles() {

        return roleRepository
                .findByRoleTypeAndDeletedFalse(
                        RoleType.GLOBAL)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * ==========================================================
     * GET ROLES BY ORGANIZATION
     * ==========================================================
     */

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getRolesByOrganization(
            UUID organizationId) {

        return roleRepository
                .findByOrganization_IdAndDeletedFalse(
                        organizationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * ==========================================================
     * DELETE ROLE
     * ==========================================================
     */

    @Override
    public void deleteRole(
            UUID roleId) {

        Role role =
                roleRepository
                        .findByIdAndDeletedFalse(roleId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Role not found."));

        /*
         * System roles cannot be deleted.
         */

        if (Boolean.TRUE.equals(
                role.getSystemRole())) {

            throw new BusinessException(
                    "System Roles cannot be deleted.");
        }

        /*
         * Soft delete
         */

        role.setDeleted(true);

        role.setActive(false);

        roleRepository.save(role);
    }

    /*
     * ==========================================================
     * ENTITY -> RESPONSE DTO
     * ==========================================================
     */

    private RoleResponseDTO mapToResponse(
            Role role) {

        UUID organizationId = null;

        if (role.getOrganization() != null) {

            organizationId =
                    role.getOrganization().getId();
        }

        return RoleResponseDTO.builder()
                .id(role.getId())
                .organizationId(organizationId)
                .roleName(role.getRoleName())
                .roleCode(role.getRoleCode())
                .description(role.getDescription())
                .roleType(role.getRoleType())
                .systemRole(role.getSystemRole())
                .active(role.getActive())
                .deleted(role.getDeleted())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    /*
     * ==========================================================
     * ROLE CODE GENERATION
     * ==========================================================
     */

    private String generateRoleCode(
            String roleName) {

        String roleCode =
                roleName
                        .trim()
                        .toUpperCase()
                        .replaceAll(
                                "[^A-Z0-9]+",
                                "_");

        /*
         * Prevent role code from exceeding 50 characters.
         */

        if (roleCode.length() > 50) {

            roleCode =
                    roleCode.substring(
                            0,
                            50);
        }

        return roleCode;
    }
}