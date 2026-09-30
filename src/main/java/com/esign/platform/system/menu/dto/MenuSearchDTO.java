package com.esign.platform.system.menu.dto;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class MenuSearchDTO {

    @Size(
            max = 150,
            message = "Search text must not exceed 150 characters"
    )
    private String search;

    private Boolean active;

    /**
     * Optional sidebar filter.
     *
     * null = all menus
     */
    private UUID sidebarId;

    /**
     * true  -> only standalone menus
     * false -> only sidebar menus
     * null  -> both
     */
    private Boolean standalone;

    @Min(
            value = 0,
            message = "Page must be greater than or equal to 0"
    )
    @Builder.Default
    private Integer page = 0;

    @Min(
            value = 1,
            message = "Size must be greater than 0"
    )
    @Max(
            value = 100,
            message = "Size must not exceed 100"
    )
    @Builder.Default
    private Integer size = 20;

    @Builder.Default
    private String sortBy = "displayOrder";

    @Builder.Default
    private String sortDirection = "ASC";
}