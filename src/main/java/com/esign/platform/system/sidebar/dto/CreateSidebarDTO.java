package com.esign.platform.system.sidebar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSidebarDTO {
	
	@NotBlank( message = "Display Name is required")
	@Size( max = 150, message = "Display name must not exceed 150 characters")
	private String displayName;
	
	@Size( max = 1000, message = " Description must not exceed 100 characters")
	private String description;
	
	@Size(max = 100 , message ="Icon must not exceed 100 characters")
	private String icon;
	
	@NotNull(message = "Display order is required")
	@Min( value = 0, message ="Display order cannot be negative")
	private Integer displayOrder;
}
