package com.esign.platform.identity.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDTO {

    private UUID id;

    private UUID employeeId;

    private String employeeCode;

    private String employeeName;

    private UUID roleId;

    private String roleName;

    private String email;

    private Boolean emailVerified;

    private Boolean accountLocked;

    private Integer failedLoginAttempts;

    private Boolean active;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createdAt;

}