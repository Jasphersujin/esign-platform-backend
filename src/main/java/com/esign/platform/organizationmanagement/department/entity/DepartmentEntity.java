package com.esign.platform.organizationmanagement.department.entity;

import com.esign.platform.common.entity.BaseEntity;
import com.esign.platform.organizationmanagement.organization.entity.OrganizationEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "departments")
public class DepartmentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "organization_id",
        nullable = false
    )
    private OrganizationEntity organization;

    @Column(
        name = "department_name",
        nullable = false,
        length = 255
    )
    private String departmentName;

    @Column(
        name = "department_code",
        nullable = false,
        length = 100
    )
    private String departmentCode;

    @Column(
        name = "department_type",
        length = 100
    )
    private String departmentType;

    @Column(
        name = "description",
        length = 1000
    )
    private String description;
}