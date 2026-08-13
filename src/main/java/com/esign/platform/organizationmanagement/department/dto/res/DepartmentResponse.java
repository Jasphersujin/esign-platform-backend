package com.esign.platform.organizationmanagement.department.dto.res;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentResponse {

    private UUID id;

    private UUID organizationId;

    private String organizationName;

    private String departmentName;

    private String departmentCode;

    private String departmentType;

    private String description;

    private Boolean active;

    private Boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long version;
}