package com.esign.platform.organizationmanagement.employee.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployeeRequestDTO {

    private UUID organizationId;

    private UUID departmentId;

    @NotBlank(message = "First Name is required.")
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email address.")
    private String email;

    @Size(max = 13)
    private String phoneNumber;

    @Size(max = 100)
    private String designation;

    private Boolean active;

}