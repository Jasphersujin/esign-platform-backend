package com.esign.platform.organizationmanagement.employeerole.dto.req;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRoleSearchRequestDTO {

    private String search;

    private UUID employeeId;

    private UUID roleId;

    private Boolean primaryRole;

    private Boolean active;

    private int page = 0;

    private int size = 20;

    private String sortBy = "createdAt";

    private String sortDirection = "DESC";
}