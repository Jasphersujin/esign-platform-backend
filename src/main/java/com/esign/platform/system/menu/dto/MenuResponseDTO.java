package com.esign.platform.system.menu.dto;

import java.time.LocalDateTime;
import java.util.UUID;

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
public class MenuResponseDTO {

    private UUID id;

    private String menuName;

    private UUID sidebarId;

    private String description;

    private String icon;

    private Integer displayOrder;

    private Boolean active;

    private Boolean deleted;

    private LocalDateTime createdAt;

    private UUID createdBy;

    private LocalDateTime updatedAt;

    private UUID updatedBy;

    private LocalDateTime deletedAt;

    private UUID deletedBy;

    private Long version;
}