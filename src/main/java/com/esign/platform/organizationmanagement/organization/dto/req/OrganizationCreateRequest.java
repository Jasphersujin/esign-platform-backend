package com.esign.platform.organizationmanagement.organization.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationCreateRequest {

    @NotBlank(message = "Organization name is required")
    @Size(max = 255)
    private String orgName;

    private byte[] orgLogo;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Address is required")
    private String addressLine1;

    private String addressLine2;

    private String postalCode;

    private String website;

    @NotBlank(message = "Business type is required")
    private String businessType;

    @Valid
    private OrganizationContactRequest contact;
}