package com.esign.platform.organizationmanagement.organization.dto.res;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationResponse {

    private UUID id;

    private String orgName;

    private byte[] orgLogo;

    private String country;

    private String state;

    private String city;

    private String addressLine1;

    private String addressLine2;

    private String postalCode;

    private String website;

    private String businessType;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<OrganizationContactResponse> contacts;
}