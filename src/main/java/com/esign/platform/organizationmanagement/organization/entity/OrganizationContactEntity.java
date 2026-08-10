package com.esign.platform.organizationmanagement.organization.entity;

import java.time.LocalDate;

import com.esign.platform.common.entity.BaseEntity;

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
@Table(name = "organization_contact")
public class OrganizationContactEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "organization_id",
        nullable = false
    )
    private OrganizationEntity organization;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "designation", length = 150)
    private String designation;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "is_primary", nullable = false)
    private Boolean primary = false;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;
}