package com.esign.platform.organizationmanagement.organization.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.esign.platform.organizationmanagement.organization.dto.req.OrganizationCreateRequest;
import com.esign.platform.organizationmanagement.organization.dto.req.OrganizationUpdateRequest;
import com.esign.platform.organizationmanagement.organization.dto.res.OrganizationResponse;

public interface OrganizationService {

    OrganizationResponse create(OrganizationCreateRequest request);

    OrganizationResponse getById(UUID id);

    Page<OrganizationResponse> getAll(String search, Pageable pageable);

    OrganizationResponse update(UUID id, OrganizationUpdateRequest request);

    void softDelete(UUID id);
}