package com.esign.platform.identity.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid Email Address.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

}