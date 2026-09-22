package com.esign.platform.system.sidebar.service;

import java.util.List;
import java.util.UUID;

import com.esign.platform.common.dto.PageResponse;
import com.esign.platform.system.sidebar.dto.CreateSidebarDTO;
import com.esign.platform.system.sidebar.dto.SidebarResponseDTO;
import com.esign.platform.system.sidebar.dto.SidebarSearchDTO;
import com.esign.platform.system.sidebar.dto.UpdateSidebarDTO;

public interface SidebarService {

    SidebarResponseDTO createSidebar(CreateSidebarDTO request);

    SidebarResponseDTO getSidebarById(UUID id);

    List<SidebarResponseDTO> getAllSidebars();

    PageResponse<SidebarResponseDTO> searchSidebars(
            SidebarSearchDTO request
    );

    SidebarResponseDTO updateSidebar(
            UUID id,
            UpdateSidebarDTO request
    );

    void activateSidebar(UUID id);

    void deactivateSidebar(UUID id);

    void deleteSidebar(UUID id);
}