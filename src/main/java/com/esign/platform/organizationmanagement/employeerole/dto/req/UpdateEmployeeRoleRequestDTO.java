package com.esign.platform.organizationmanagement.employeerole.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployeeRoleRequestDTO {

    @NotNull(message = "Primary role is required")
    private Boolean primaryRole;
}