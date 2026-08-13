package com.esign.platform.organizationmanagement.department.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esign.platform.organizationmanagement.department.entity.DepartmentEntity;

public interface DepartmentRepository
        extends JpaRepository<
            DepartmentEntity,
            UUID
        >,
        JpaSpecificationExecutor<DepartmentEntity> {

    Optional<DepartmentEntity> findByIdAndDeletedFalse(
        UUID id
    );

    boolean existsByOrganization_IdAndDepartmentCodeIgnoreCase(
        UUID organizationId,
        String departmentCode
    );

    boolean existsByOrganization_IdAndDepartmentCodeIgnoreCaseAndIdNot(
        UUID organizationId,
        String departmentCode,
        UUID id
    );

    List<DepartmentEntity> findByOrganization_IdAndDeletedFalse(
        UUID organizationId
    );
}