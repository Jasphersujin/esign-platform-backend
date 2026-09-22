package com.esign.platform.system.sidebar.entity;

import com.esign.platform.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "sidebar_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SidebarEntity extends BaseEntity {
	
	@Column(name = "display_name", nullable = false, length = 150)
	private String displayName;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "icon", length = 150)
	private String icon;
	
	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;
	
}	
