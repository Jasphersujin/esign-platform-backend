package com.esign.platform.organizationmanagement.organization.dto.res;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationContactResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String designation;

    private String email;

    private String countryCode;

    private String phoneNumber;

    private Boolean primary;

    private Boolean active;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;
}