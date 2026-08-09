package com.esign.platform.organizationmanagement.employee.dto.res;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDTO {

    private UUID id;

    private UUID organizationId;

    private UUID departmentId;

    private String employeeCode;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String designation;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}