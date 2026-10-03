package com.esign.platform.system.menu.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esign.platform.common.dto.ApiResponse;
import com.esign.platform.common.dto.PageResponse;
import com.esign.platform.system.menu.dto.CreateMenuDTO;
import com.esign.platform.system.menu.dto.MenuResponseDTO;
import com.esign.platform.system.menu.dto.MenuSearchDTO;
import com.esign.platform.system.menu.dto.UpdateMenuDTO;
import com.esign.platform.system.menu.service.MenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Menu Management",
        description = "APIs for managing application menus"
)
public class MenuController {

    private final MenuService menuService;

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    @Operation(
            summary = "Create menu",
            description = """
                    Creates a new menu.

                    A menu may optionally belong to a sidebar.
                    If sidebarId is null, the menu is treated as
                    a standalone menu.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Menu created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid menu data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Sidebar not found"
            )
    })
    public ResponseEntity<ApiResponse<MenuResponseDTO>>
    createMenu(
            @Valid @RequestBody CreateMenuDTO request
    ) {

        log.info(
                "REST request to create menu: {}",
                request.getMenuName()
        );

        MenuResponseDTO data =
                menuService.createMenu(request);

        ApiResponse<MenuResponseDTO> response =
                ApiResponse.<MenuResponseDTO>builder()
                        .success(true)
                        .message(
                                "Menu created successfully"
                        )
                        .data(data)
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    @Operation(
            summary = "Get all menus",
            description = """
                    Returns all non-deleted menus.
                    Results are ordered by display order.
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Menus retrieved successfully"
    )
    public ResponseEntity<
            ApiResponse<List<MenuResponseDTO>>
            > getAllMenus() {

        log.debug(
                "REST request to get all menus"
        );

        List<MenuResponseDTO> data =
                menuService.getAllMenus();

        ApiResponse<List<MenuResponseDTO>> response =
                ApiResponse.<List<MenuResponseDTO>>builder()
                        .success(true)
                        .message(
                                "Menus retrieved successfully"
                        )
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // SEARCH
    // =========================================================

    @PostMapping("/search")
    @Operation(
            summary = "Search menus",
            description = """
                    Search menus by name, filter by active status,
                    sidebar, standalone status, paginate and sort.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters"
            )
    })
    public ResponseEntity<
            ApiResponse<PageResponse<MenuResponseDTO>>
            > searchMenus(
            @Parameter(
                    description =
                            "Menu search, filter and pagination parameters"
            )
            @Valid MenuSearchDTO request
    ) {

        log.debug(
                "REST request to search menus"
        );

        PageResponse<MenuResponseDTO> data =
                menuService.searchMenus(request);

        ApiResponse<PageResponse<MenuResponseDTO>> response =
                ApiResponse
                        .<PageResponse<MenuResponseDTO>>builder()
                        .success(true)
                        .message(
                                "Menus retrieved successfully"
                        )
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get menu by ID",
            description = "Returns a menu by UUID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Menu retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Menu not found"
            )
    })
    public ResponseEntity<
            ApiResponse<MenuResponseDTO>
            > getMenuById(

            @Parameter(
                    description = "Menu UUID",
                    required = true
            )
            @PathVariable UUID id
    ) {

        log.debug(
                "REST request to get menu. id={}",
                id
        );

        MenuResponseDTO data =
                menuService.getMenuById(id);

        ApiResponse<MenuResponseDTO> response =
                ApiResponse.<MenuResponseDTO>builder()
                        .success(true)
                        .message(
                                "Menu retrieved successfully"
                        )
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    @Operation(
            summary = "Update menu",
            description = """
                    Updates an existing menu.

                    sidebarId may be changed to move a menu
                    between sidebars or set it to null to make
                    the menu standalone.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Menu updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid menu data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Menu or sidebar not found"
            )
    })
    public ResponseEntity<
            ApiResponse<MenuResponseDTO>
            > updateMenu(

            @Parameter(
                    description = "Menu UUID",
                    required = true
            )
            @PathVariable UUID id,

            @Valid @RequestBody UpdateMenuDTO request
    ) {

        log.info(
                "REST request to update menu. id={}",
                id
        );

        MenuResponseDTO data =
                menuService.updateMenu(
                        id,
                        request
                );

        ApiResponse<MenuResponseDTO> response =
                ApiResponse.<MenuResponseDTO>builder()
                        .success(true)
                        .message(
                                "Menu updated successfully"
                        )
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ACTIVATE
    // =========================================================

    @PutMapping("/{id}/activate")
    @Operation(
            summary = "Activate menu",
            description = "Activates a menu"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Menu activated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Menu not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>>
    activateMenu(

            @Parameter(
                    description = "Menu UUID",
                    required = true
            )
            @PathVariable UUID id
    ) {

        log.info(
                "REST request to activate menu. id={}",
                id
        );

        menuService.activateMenu(id);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Menu activated successfully"
                        )
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @PutMapping("/{id}/deactivate")
    @Operation(
            summary = "Deactivate menu",
            description = "Deactivates a menu"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Menu deactivated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Menu not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>>
    deactivateMenu(

            @Parameter(
                    description = "Menu UUID",
                    required = true
            )
            @PathVariable UUID id
    ) {

        log.info(
                "REST request to deactivate menu. id={}",
                id
        );

        menuService.deactivateMenu(id);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Menu deactivated successfully"
                        )
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete menu",
            description = "Soft deletes a menu"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Menu deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Menu not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>>
    deleteMenu(

            @Parameter(
                    description = "Menu UUID",
                    required = true
            )
            @PathVariable UUID id
    ) {

        log.info(
                "REST request to delete menu. id={}",
                id
        );

        menuService.deleteMenu(id);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Menu deleted successfully"
                        )
                        .build();

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/sidebar/{sidebarId}")
    @Operation(
            summary = "Get menus by sidebar",
            description = """
                    Retrieves all active, non-deleted menus
                    belonging to the specified sidebar.
                    Results are ordered by display order.
                    """
    )
    @ApiResponses({
    	@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Menus retrieved successfully"
            ),
    	@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Sidebar not found"
            ),
    	@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid sidebar ID"
            )
    })
    public ResponseEntity<
            ApiResponse<List<MenuResponseDTO>>
    > getMenusBySidebar(
            @PathVariable UUID sidebarId
    ) {

        log.debug(
                "REST request to get menus for sidebar: {}",
                sidebarId
        );

        List<MenuResponseDTO> data =
                menuService.getMenusBySidebarId(sidebarId);

        ApiResponse<List<MenuResponseDTO>> response =
                ApiResponse
                        .<List<MenuResponseDTO>>builder()
                        .success(true)
                        .message("Menus retrieved successfully")
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }
}