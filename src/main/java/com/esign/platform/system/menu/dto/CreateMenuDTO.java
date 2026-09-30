package com.esign.platform.system.menu.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMenuDTO {

    @NotBlank(message = "Menu name is required")
    @Size(
            max = 150,
            message = "Menu name must not exceed 150 characters"
    )
    private String menuName;

    /**
     * Optional.
     *
     * null = standalone menu
     */
    private UUID sidebarId;

    @Size(
            max = 5000,
            message = "Description must not exceed 5000 characters"
    )
    private String description;

    @Size(
            max = 150,
            message = "Icon must not exceed 150 characters"
    )
    private String icon;

    @Min(
            value = 0,
            message = "Display order must be greater than or equal to 0"
    )
    private Integer displayOrder;
}