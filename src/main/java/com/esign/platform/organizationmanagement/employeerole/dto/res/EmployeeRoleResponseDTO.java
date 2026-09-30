package com.esign.platform.organizationmanagement.employeerole.dto.res;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRoleResponseDTO {

    private UUID id;

    private UUID employeeId;

    private String employeeCode;

    private String employeeName;

    private String employeeEmail;

    private UUID roleId;

    private String roleName;

    private String roleCode;

    private Boolean primaryRole;

    private LocalDateTime assignedAt;

    private UUID assignedBy;

    private Boolean active;

    private LocalDateTime createdAt;

    private UUID createdBy;

    private LocalDateTime updatedAt;

    private UUID updatedBy;
}