package com.esign.platform.accesscontrol.role.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.esign.platform.accesscontrol.role.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {

    private UUID id;

    private UUID organizationId;

    private String roleName;

    private String description;

    private RoleType roleType;

    private Boolean systemRole;

    private Boolean active;

    private Boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}