package com.esign.platform.organizationmanagement.organization.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationContactRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    private String designation;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    private String countryCode;

    private String phoneNumber;
}