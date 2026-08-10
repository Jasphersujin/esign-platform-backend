package com.esign.platform.organizationmanagement.organization.entity;

import java.util.ArrayList;
import java.util.List;

import com.esign.platform.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organization")
public class OrganizationEntity extends BaseEntity {

    @Column(name = "org_name", nullable = false, length = 255)
    private String orgName;

    @Lob
    @Column(name = "org_logo")
    private byte[] orgLogo;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "state", nullable = false, length = 100)
    private String state;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "address_line_1", nullable = false, length = 500)
    private String addressLine1;

    @Column(name = "address_line_2", length = 500)
    private String addressLine2;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "website", length = 500)
    private String website;

    @Column(name = "business_type", nullable = false, length = 100)
    private String businessType;

    @OneToMany(
        mappedBy = "organization",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<OrganizationContactEntity> contacts = new ArrayList<>();
}