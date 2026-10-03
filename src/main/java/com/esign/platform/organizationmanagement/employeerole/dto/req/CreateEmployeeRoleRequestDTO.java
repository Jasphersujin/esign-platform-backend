package com.esign.platform.organizationmanagement.employeerole.dto.req;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRoleRequestDTO {

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotNull(message = "Role ID is required")
    private UUID roleId;

    private Boolean primaryRole = false;
}