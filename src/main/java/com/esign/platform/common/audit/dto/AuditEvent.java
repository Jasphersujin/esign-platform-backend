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
public class AuditEvent {

    private UUID organizationId;

    private UUID userId;

    private UUID employeeId;

    private String action;

    private String entityType;

    private UUID entityId;

    private String description;

    private String requestMethod;

    private String requestUri;

    private String ipAddress;

    private String userAgent;

    private Object beforeData;

    private Object afterData;

    private String status;

    private String errorMessage;

    private String correlationId;

    private Instant createdAt;
}