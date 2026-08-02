package com.esign.platform.identity.user.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

    @NotNull(message = "Role is required.")
    private UUID roleId;

    private Boolean accountLocked;

    private Boolean active;

}