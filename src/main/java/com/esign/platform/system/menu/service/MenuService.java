package com.esign.platform.system.menu.service;

import java.util.List;
import java.util.UUID;

import com.esign.platform.common.dto.PageResponse;
import com.esign.platform.system.menu.dto.CreateMenuDTO;
import com.esign.platform.system.menu.dto.MenuResponseDTO;
import com.esign.platform.system.menu.dto.MenuSearchDTO;
import com.esign.platform.system.menu.dto.UpdateMenuDTO;

public interface MenuService {

    MenuResponseDTO createMenu(CreateMenuDTO request);

    MenuResponseDTO getMenuById(UUID id);

    List<MenuResponseDTO> getAllMenus();

    PageResponse<MenuResponseDTO> searchMenus(
            MenuSearchDTO request
    );

    MenuResponseDTO updateMenu(
            UUID id,
            UpdateMenuDTO request
    );

    void activateMenu(UUID id);

    void deactivateMenu(UUID id);

    void deleteMenu(UUID id);
}