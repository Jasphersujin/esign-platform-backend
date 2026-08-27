package com.esign.platform.accesscontrol.role;

import com.esign.platform.common.entity.BaseEntity;
import com.esign.platform.organizationmanagement.organization.entity.OrganizationEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    /**
     * Organization to which this role belongs.
     *
     * GLOBAL role  -> organization = null
     * TENANT role  -> organization is required
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "organization_id",
        nullable = true
    )
    private OrganizationEntity organization;

    @Column(
        name = "role_name",
        nullable = false,
        length = 100
    )
    private String roleName;

    @Column(
        name = "role_code",
        nullable = false,
        length = 50
    )
    private String roleCode;

    @Column(
        name = "description",
        length = 500
    )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "role_type",
        nullable = false
    )
    private RoleType roleType;

    @Column(
        name = "system_role",
        nullable = false
    )
    private Boolean systemRole = false;
}