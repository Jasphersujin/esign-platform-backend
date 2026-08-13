package com.esign.platform.organizationmanagement.department.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.esign.platform.organizationmanagement.department.dto.req.DepartmentCreateRequest;
import com.esign.platform.organizationmanagement.department.dto.req.DepartmentUpdateRequest;
import com.esign.platform.organizationmanagement.department.dto.res.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse create(
        DepartmentCreateRequest request
    );

    DepartmentResponse getById(
        UUID id
    );

    DepartmentResponse update(
        UUID id,
        DepartmentUpdateRequest request
    );

    void delete(
        UUID id
    );

    void restore(
        UUID id
    );

    List<DepartmentResponse> getByOrganization(
        UUID organizationId
    );

    Page<DepartmentResponse> search(
        UUID organizationId,
        String search,
        String departmentName,
        String departmentCode,
        String departmentType,
        Boolean active,
        Boolean deleted,
        Boolean includeDeleted,
        LocalDate createdFrom,
        LocalDate createdTo,
        LocalDate updatedFrom,
        LocalDate updatedTo,
        int page,
        int size,
        String sortBy,
        String sortDirection
    );
}