package com.esign.platform.common.audit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "audit_trail",
        indexes = {
                @Index(
                        name = "idx_audit_trail_org",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_audit_trail_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_audit_trail_employee",
                        columnList = "employee_id"
                ),
                @Index(
                        name = "idx_audit_trail_action",
                        columnList = "action"
                ),
                @Index(
                        name = "idx_audit_trail_entity",
                        columnList = "entity_type, entity_id"
                ),
                @Index(
                        name = "idx_audit_trail_created_at",
                        columnList = "created_at"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            columnDefinition = "BINARY(16)"
    )
    private UUID id;

    @Column(
            name = "organization_id",
            columnDefinition = "BINARY(16)"
    )
    private UUID organizationId;

    @Column(
            name = "user_id",
            columnDefinition = "BINARY(16)"
    )
    private UUID userId;

    @Column(
            name = "employee_id",
            columnDefinition = "BINARY(16)"
    )
    private UUID employeeId;

    @Column(
            name = "action",
            nullable = false,
            length = 100
    )
    private String action;

    @Column(
            name = "entity_type",
            length = 100
    )
    private String entityType;

    @Column(
            name = "entity_id",
            columnDefinition = "BINARY(16)"
    )
    private UUID entityId;

    @Column(
            name = "description",
            length = 500
    )
    private String description;

    @Column(
            name = "request_method",
            length = 20
    )
    private String requestMethod;

    @Column(
            name = "request_uri",
            length = 1000
    )
    private String requestUri;

    @Column(
            name = "ip_address",
            length = 100
    )
    private String ipAddress;

    @Column(
            name = "user_agent",
            columnDefinition = "TEXT"
    )
    private String userAgent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "before_data",
            columnDefinition = "JSON"
    )
    private Object beforeData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "after_data",
            columnDefinition = "JSON"
    )
    private Object afterData;

    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private String status;

    @Column(
            name = "error_message",
            columnDefinition = "TEXT"
    )
    private String errorMessage;

    @Column(
            name = "correlation_id",
            length = 100
    )
    private String correlationId;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;
}