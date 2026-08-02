package com.esign.platform.accesscontrol.role.dto;

import java.util.UUID;

import com.esign.platform.accesscontrol.role.RoleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequestDTO {

    private UUID organizationId;

    @NotBlank(message = "Role Name is required.")
    @Size(max = 100, message = "Role Name cannot exceed 100 characters.")
    private String roleName;

    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

    @NotNull(message = "Role Type is required.")
    private RoleType roleType;

    private Boolean systemRole = false;

}