package com.esign.platform.organizationmanagement.organization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.esign.platform.organizationmanagement.organization.entity.OrganizationEntity;

public interface OrganizationRepository
        extends JpaRepository<OrganizationEntity, UUID> {

    Optional<OrganizationEntity> findByIdAndDeletedFalse(UUID id);

    Page<OrganizationEntity> findByDeletedFalse(
            Pageable pageable
    );

    Page<OrganizationEntity> findByDeletedFalseAndOrgNameContainingIgnoreCase(
            String orgName,
            Pageable pageable
    );
}