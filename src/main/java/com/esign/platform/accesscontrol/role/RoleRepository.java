package com.esign.platform.accesscontrol.role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByIdAndDeletedFalse(UUID id);

    Optional<Role> findByRoleCodeAndDeletedFalse(
            String roleCode);

    /*
     * Global role duplicate check
     */
    boolean existsByRoleTypeAndRoleNameIgnoreCaseAndIdNot(
            RoleType roleType,
            String roleName,
            UUID roleId);

    /*
     * Global role duplicate check for CREATE
     */
    boolean existsByRoleTypeAndRoleNameIgnoreCase(
            RoleType roleType,
            String roleName);

    /*
     * Tenant role duplicate check for CREATE
     */
    boolean existsByOrganization_IdAndRoleNameIgnoreCase(
            UUID organizationId,
            String roleName);

    /*
     * Tenant role duplicate check for UPDATE
     */
    boolean existsByOrganization_IdAndRoleNameIgnoreCaseAndIdNot(
            UUID organizationId,
            String roleName,
            UUID roleId);

    /*
     * Get all roles for organization
     */
    List<Role> findByOrganization_IdAndDeletedFalse(
            UUID organizationId);

    /*
     * Get roles by type
     */
    List<Role> findByRoleTypeAndDeletedFalse(
            RoleType roleType);

    /*
     * Get all non-deleted roles
     */
    List<Role> findByDeletedFalse();

    /*
     * Find role by type + name
     */
    Optional<Role>
    findByRoleTypeAndRoleNameIgnoreCaseAndDeletedFalse(
            RoleType roleType,
            String roleName);
}