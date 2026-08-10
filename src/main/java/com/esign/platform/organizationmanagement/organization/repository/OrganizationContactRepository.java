package com.esign.platform.organizationmanagement.organization.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esign.platform.organizationmanagement.organization.entity.OrganizationContactEntity;

public interface OrganizationContactRepository
        extends JpaRepository<OrganizationContactEntity, UUID> {

    List<OrganizationContactEntity>
    findByOrganizationIdAndDeletedFalse(UUID organizationId);

    Optional<OrganizationContactEntity>
    findByIdAndOrganizationIdAndDeletedFalse(
            UUID id,
            UUID organizationId
    );
}