package com.esign.platform.organizationmanagement.employeerole.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.esign.platform.accesscontrol.role.Role;
import com.esign.platform.common.entity.BaseEntity;
import com.esign.platform.organizationmanagement.employee.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "employee_roles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_employee_role",
            columnNames = {"employee_id", "role_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRoleEntity extends BaseEntity {

    /**
     * Employee to whom the role is assigned.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "employee_id",
        nullable = false
    )
    private Employee employee;

    /**
     * Role assigned to the employee.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "role_id",
        nullable = false
    )
    private Role role;

    /**
     * Date and time when the role was assigned.
     */
    @Column(
        name = "assigned_at",
        nullable = false
    )
    private LocalDateTime assignedAt;

    /**
     * Employee/admin who assigned this role.
     */
    @Column(
        name = "assigned_by"
    )
    private UUID assignedBy;

    /**
     * Indicates whether this is the employee's primary role.
     */
    @Column(
        name = "primary_role",
        nullable = false
    )
    private Boolean primaryRole = false;
}