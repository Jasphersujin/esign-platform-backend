package com.esign.platform.common.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditTrailSearchRequest {

    private UUID organizationId;

    private UUID userId;

    private UUID employeeId;

    private String action;

    private String entityType;

    private UUID entityId;

    private String status;

    private Instant from;

    private Instant to;

    private String search;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "DESC";
}