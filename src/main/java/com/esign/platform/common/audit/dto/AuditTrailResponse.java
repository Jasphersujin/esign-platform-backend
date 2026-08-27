package com.esign.platform.common.audit.dto;

import com.esign.platform.common.audit.entity.AuditTrail;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AuditTrailResponse {

    private UUID id;

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

    public static AuditTrailResponse from(
            AuditTrail audit
    ) {

        return AuditTrailResponse.builder()

                .id(audit.getId())

                .organizationId(
                        audit.getOrganizationId()
                )

                .userId(
                        audit.getUserId()
                )

                .employeeId(
                        audit.getEmployeeId()
                )

                .action(
                        audit.getAction()
                )

                .entityType(
                        audit.getEntityType()
                )

                .entityId(
                        audit.getEntityId()
                )

                .description(
                        audit.getDescription()
                )

                .requestMethod(
                        audit.getRequestMethod()
                )

                .requestUri(
                        audit.getRequestUri()
                )

                .ipAddress(
                        audit.getIpAddress()
                )

                .userAgent(
                        audit.getUserAgent()
                )

                .beforeData(
                        audit.getBeforeData()
                )

                .afterData(
                        audit.getAfterData()
                )

                .status(
                        audit.getStatus()
                )

                .errorMessage(
                        audit.getErrorMessage()
                )

                .correlationId(
                        audit.getCorrelationId()
                )

                .createdAt(
                        audit.getCreatedAt()
                )

                .build();
    }
}