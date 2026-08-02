package com.esign.platform.accesscontrol.role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByIdAndDeletedFalse(UUID id);

    boolean existsByRoleTypeAndRoleNameIgnoreCase(
            RoleType roleType,
            String roleName);

    boolean existsByOrganizationIdAndRoleNameIgnoreCase(
            UUID organizationId,
            String roleName);

    List<Role> findByOrganizationIdAndDeletedFalse(
            UUID organizationId);

    List<Role> findByRoleTypeAndDeletedFalse(
            RoleType roleType);

    List<Role> findByDeletedFalse();

}