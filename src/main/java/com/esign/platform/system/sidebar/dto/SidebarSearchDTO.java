package com.esign.platform.system.sidebar.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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
public class SidebarSearchDTO {

    /**
     * Searches sidebar display name.
     */
    private String search;

    /**
     * true  = active only
     * false = inactive only
     * null  = both
     */
    private Boolean active;

    /**
     * Zero-based page number.
     */
    @Min(value = 0, message = "Page cannot be negative")
    @Builder.Default
    private Integer page = 0;

    /**
     * Number of records per page.
     */
    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 100, message = "Size cannot exceed 100")
    @Builder.Default
    private Integer size = 10;

    /**
     * Field used for sorting.
     */
    @Builder.Default
    private String sortBy = "displayOrder";

    /**
     * ASC or DESC.
     */
    @Builder.Default
    private String sortDirection = "ASC";
}