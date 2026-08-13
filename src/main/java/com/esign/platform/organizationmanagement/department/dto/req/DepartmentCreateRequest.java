package com.esign.platform.organizationmanagement.department.dto.req;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentCreateRequest {

    @NotNull(message = "Organization ID is required")
    private UUID organizationId;

    @NotBlank(message = "Department name is required")
    @Size(
        max = 255,
        message = "Department name must not exceed 255 characters"
    )
    private String departmentName;

    @NotBlank(message = "Department code is required")
    @Size(
        max = 100,
        message = "Department code must not exceed 100 characters"
    )
    private String departmentCode;

    @Size(
        max = 100,
        message = "Department type must not exceed 100 characters"
    )
    private String departmentType;

    @Size(
        max = 1000,
        message = "Description must not exceed 1000 characters"
    )
    private String description;

    private Boolean active;
}