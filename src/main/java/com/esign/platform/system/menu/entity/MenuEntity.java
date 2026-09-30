package com.esign.platform.system.menu.entity;

import java.util.UUID;

import com.esign.platform.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "menus",
        indexes = {
                @Index(
                        name = "idx_menus_sidebar_id",
                        columnList = "sidebar_id"
                ),
                @Index(
                        name = "idx_menus_active",
                        columnList = "active"
                ),
                @Index(
                        name = "idx_menus_deleted",
                        columnList = "deleted"
                ),
                @Index(
                        name = "idx_menus_display_order",
                        columnList = "display_order"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuEntity extends BaseEntity {

    @Column(
            name = "menu_name",
            nullable = false,
            length = 150
    )
    private String menuName;

    /**
     * Nullable.
     *
     * NULL  -> Standalone menu
     * UUID  -> Menu belongs to a sidebar
     */
    @Column(
            name = "sidebar_id"
    )
    private UUID sidebarId;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "icon",
            length = 150
    )
    private String icon;

    @Column(
            name = "display_order",
            nullable = false
    )
    private Integer displayOrder;
}