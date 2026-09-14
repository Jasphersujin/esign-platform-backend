package com.esign.platform.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class PageRequestDTO {

	@Min(value = 0, message = "Page cannot be negative")
	@Builder.Default
	private Integer page = 0;
	
	@Min(value = 1, message = "Size must be atleast 1")
	@Max( value = 100, message = "Size cannot exceed 100")
	@Builder.Default
	private Integer size = 10;
	
	@Builder.Default
	private String sortBy = "createdAt";
	
    @Builder.Default
    private String sortDirection = "DESC";
    
    private String search;
	
	
}
