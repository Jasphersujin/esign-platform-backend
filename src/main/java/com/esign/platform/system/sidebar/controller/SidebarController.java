//package com.esign.platform.system.sidebar.controller;
//
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.esign.platform.common.dto.ApiResponse;
//import com.esign.platform.common.dto.PageResponse;
//import com.esign.platform.system.sidebar.dto.CreateSidebarDTO;
//import com.esign.platform.system.sidebar.dto.SidebarResponseDTO;
//import com.esign.platform.system.sidebar.dto.SidebarSearchDTO;
//import com.esign.platform.system.sidebar.dto.UpdateSidebarDTO;
//import com.esign.platform.system.sidebar.service.SidebarService;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/api/v1/sidebars")
//@RequiredArgsConstructor
//@Slf4j
//@Tag(
//        name = "Sidebar Management",
//        description = "APIs for managing sidebar sections"
//)
//public class SidebarController {
//
//    private final SidebarService sidebarService;
//
//    // =========================================================
//    // CREATE
//    // =========================================================
//
//    @PostMapping
//    @Operation(
//            summary = "Create sidebar",
//            description = "Creates a new sidebar section"
//    )
//    @ApiResponses({
//            @SwaggerApiResponse(
//                    responseCode = "201",
//                    description = "Sidebar created successfully"
//            ),
//            @SwaggerApiResponse(
//                    responseCode = "400",
//                    description = "Invalid sidebar data"
//            )
//    })
//    public ResponseEntity<ApiResponse<SidebarResponseDTO>> createSidebar(
//            @Valid @RequestBody CreateSidebarDTO request
//    ) {
//
//        log.info(
//                "REST request to create sidebar: {}",
//                request.getDisplayName()
//        );
//
//        SidebarResponseDTO data =
//                sidebarService.createSidebar(request);
//
//        ApiResponse<SidebarResponseDTO> response =
//                ApiResponse.<SidebarResponseDTO>builder()
//                        .success(true)
//                        .message("Sidebar created successfully")
//                        .data(data)
//                        .build();
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//    // =========================================================
//    // GET ALL
//    // =========================================================
//
//    @GetMapping
//    @Operation(
//            summary = "Get all sidebars",
//            description = "Returns all non-deleted sidebar sections"
//    )
//    @ApiResponse(
//            responseCode = "200",
//            description = "Sidebars retrieved successfully"
//    )
//    public ResponseEntity<ApiResponse<List<SidebarResponseDTO>>>
//    getAllSidebars() {
//
//        log.debug("REST request to get all sidebars");
//
//        List<SidebarResponseDTO> data =
//                sidebarService.getAllSidebars();
//
//        ApiResponse<List<SidebarResponseDTO>> response =
//                ApiResponse.<List<SidebarResponseDTO>>builder()
//                        .success(true)
//                        .message("Sidebars retrieved successfully")
//                        .data(data)
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // =========================================================
//    // SEARCH
//    // =========================================================
//
//    @GetMapping("/search")
//    @Operation(
//            summary = "Search sidebars",
//            description = """
//                    Search sidebar sections by display name,
//                    filter by active status, paginate and sort results.
//                    """
//    )
//    @ApiResponses({
//            @SwaggerApiResponse(
//                    responseCode = "200",
//                    description = "Search completed successfully"
//            ),
//            @SwaggerApiResponse(
//                    responseCode = "400",
//                    description = "Invalid search parameters"
//            )
//    })
//    public ResponseEntity<
//            ApiResponse<PageResponse<SidebarResponseDTO>>
//            > searchSidebars(
//
//            @Parameter(
//                    description = "Search and pagination parameters"
//            )
//            @Valid SidebarSearchDTO request
//    ) {
//
//        log.debug(
//                "REST request to search sidebars"
//        );
//
//        PageResponse<SidebarResponseDTO> data =
//                sidebarService.searchSidebars(request);
//
//        ApiResponse<PageResponse<SidebarResponseDTO>> response =
//                ApiResponse
//                        .<PageResponse<SidebarResponseDTO>>builder()
//                        .success(true)
//                        .message("Sidebars retrieved successfully")
//                        .data(data)
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // =========================================================
//    // GET BY ID
//    // =========================================================
//
//    @GetMapping("/{id}")
//    @Operation(
//            summary = "Get sidebar by ID",
//            description = "Returns a sidebar section by UUID"
//    )
//    @ApiResponses({
//            @SwaggerApiResponse(
//                    responseCode = "200",
//                    description = "Sidebar retrieved successfully"
//            ),
//            @SwaggerApiResponse(
//                    responseCode = "404",
//                    description = "Sidebar not found"
//            )
//    })
//    public ResponseEntity<ApiResponse<SidebarResponseDTO>>
//    getSidebarById(
//
//            @Parameter(
//                    description = "Sidebar UUID",
//                    required = true
//            )
//            @PathVariable UUID id
//    ) {
//
//        log.debug(
//                "REST request to get sidebar. id={}",
//                id
//        );
//
//        SidebarResponseDTO data =
//                sidebarService.getSidebarById(id);
//
//        ApiResponse<SidebarResponseDTO> response =
//                ApiResponse.<SidebarResponseDTO>builder()
//                        .success(true)
//                        .message("Sidebar retrieved successfully")
//                        .data(data)
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // =========================================================
//    // UPDATE
//    // =========================================================
//
//    @PutMapping("/{id}")
//    @Operation(
//            summary = "Update sidebar",
//            description = "Updates an existing sidebar section"
//    )
//    @ApiResponses({
//            @SwaggerApiResponse(
//                    responseCode = "200",
//                    description = "Sidebar updated successfully"
//            ),
//            @SwaggerApiResponse(
//                    responseCode = "400",
//                    description = "Invalid sidebar data"
//            ),
//            @SwaggerApiResponse(
//                    responseCode = "404",
//                    description = "Sidebar not found"
//            )
//    })
//    public ResponseEntity<ApiResponse<SidebarResponseDTO>>
//    updateSidebar(
//
//            @Parameter(
//                    description = "Sidebar UUID",
//                    required = true
//            )
//            @PathVariable UUID id,
//
//            @Valid @RequestBody UpdateSidebarDTO request
//    ) {
//
//        log.info(
//                "REST request to update sidebar. id={}",
//                id
//        );
//
//        SidebarResponseDTO data =
//                sidebarService.updateSidebar(
//                        id,
//                        request
//                );
//
//        ApiResponse<SidebarResponseDTO> response =
//                ApiResponse.<SidebarResponseDTO>builder()
//                        .success(true)
//                        .message("Sidebar updated successfully")
//                        .data(data)
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // =========================================================
//    // ACTIVATE
//    // =========================================================
//
//    @PutMapping("/{id}/activate")
//    @Operation(
//            summary = "Activate sidebar",
//            description = "Activates a sidebar section"
//    )
//    @ApiResponses({
//            @SwaggerApiResponse(
//                    responseCode = "200",
//                    description = "Sidebar activated successfully"
//            ),
//            @SwaggerApiResponse(
//                    responseCode = "404",
//                    description = "Sidebar not found"
//            )
//    })
//    public ResponseEntity<ApiResponse<Void>>
//    activateSidebar(
//
//            @Parameter(
//                    description = "Sidebar UUID",
//                    required = true
//            )
//            @PathVariable UUID id
//    ) {
//
//        log.info(
//                "REST request to activate sidebar. id={}",
//                id
//        );
//
//        sidebarService.activateSidebar(id);
//
//        ApiResponse<Void> response =
//                ApiResponse.<Void>builder()
//                        .success(true)
//                        .message("Sidebar activated successfully")
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // =========================================================
//    // DEACTIVATE
//    // =========================================================
//
//    @PutMapping("/{id}/deactivate")
//    @Operation(
//            summary = "Deactivate sidebar",
//            description = "Deactivates a sidebar section"
//    )
//    @ApiResponses({
//            @SwaggerApiResponse(
//                    responseCode = "200",
//                    description = "Sidebar deactivated successfully"
//            ),
//            @SwaggerApiResponse(
//                    responseCode = "404",
//                    description = "Sidebar not found"
//            )
//    })
//    public ResponseEntity<ApiResponse<Void>>
//    deactivateSidebar(
//
//            @Parameter(
//                    description = "Sidebar UUID",
//                    required = true
//            )
//            @PathVariable UUID id
//    ) {
//
//        log.info(
//                "REST request to deactivate sidebar. id={}",
//                id
//        );
//
//        sidebarService.deactivateSidebar(id);
//
//        ApiResponse<Void> response =
//                ApiResponse.<Void>builder()
//                        .success(true)
//                        .message("Sidebar deactivated successfully")
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // =========================================================
//    // DELETE
//    // =========================================================
//
//    @DeleteMapping("/{id}")
//    @Operation(
//            summary = "Delete sidebar",
//            description = "Soft deletes a sidebar section"
//    )
//    @ApiResponses({
//            @SwaggerApiResponse(
//                    responseCode = "204",
//                    description = "Sidebar deleted successfully"
//            ),
//            @SwaggerApiResponse(
//                    responseCode = "404",
//                    description = "Sidebar not found"
//            )
//    })
//    public ResponseEntity<ApiResponse<Void>>
//    deleteSidebar(
//
//            @Parameter(
//                    description = "Sidebar UUID",
//                    required = true
//            )
//            @PathVariable UUID id
//    ) {
//
//        log.info(
//                "REST request to delete sidebar. id={}",
//                id
//        );
//
//        sidebarService.deleteSidebar(id);
//
//        ApiResponse<Void> response =
//                ApiResponse.<Void>builder()
//                        .success(true)
//                        .message("Sidebar deleted successfully")
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }
//}

package com.esign.platform.system.sidebar.controller;

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
import com.esign.platform.system.sidebar.dto.CreateSidebarDTO;
import com.esign.platform.system.sidebar.dto.SidebarResponseDTO;
import com.esign.platform.system.sidebar.dto.SidebarSearchDTO;
import com.esign.platform.system.sidebar.dto.UpdateSidebarDTO;
import com.esign.platform.system.sidebar.service.SidebarService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/sidebars")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Sidebar Management",
        description = "APIs for managing sidebar sections"
)
public class SidebarController {

    private final SidebarService sidebarService;

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    @Operation(
            summary = "Create sidebar",
            description = "Creates a new sidebar section"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Sidebar created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid sidebar data"
            )
    })
    public ResponseEntity<ApiResponse<SidebarResponseDTO>> createSidebar(
            @Valid @RequestBody CreateSidebarDTO request
    ) {

        log.info(
                "REST request to create sidebar: {}",
                request.getDisplayName()
        );

        SidebarResponseDTO data =
                sidebarService.createSidebar(request);

        ApiResponse<SidebarResponseDTO> response =
                ApiResponse.<SidebarResponseDTO>builder()
                        .success(true)
                        .message("Sidebar created successfully")
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
            summary = "Get all sidebars",
            description = "Returns all non-deleted sidebar sections"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Sidebars retrieved successfully"
    )
    public ResponseEntity<ApiResponse<List<SidebarResponseDTO>>>
    getAllSidebars() {

        log.debug("REST request to get all sidebars");

        List<SidebarResponseDTO> data =
                sidebarService.getAllSidebars();

        ApiResponse<List<SidebarResponseDTO>> response =
                ApiResponse.<List<SidebarResponseDTO>>builder()
                        .success(true)
                        .message("Sidebars retrieved successfully")
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // SEARCH
    // =========================================================

    @GetMapping("/search")
    @Operation(
            summary = "Search sidebars",
            description = """
                    Search sidebar sections by display name,
                    filter by active status, paginate and sort results.
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
            ApiResponse<PageResponse<SidebarResponseDTO>>
    > searchSidebars(
            @Parameter(
                    description = "Search and pagination parameters"
            )
            @Valid SidebarSearchDTO request
    ) {

        log.debug("REST request to search sidebars");

        PageResponse<SidebarResponseDTO> data =
                sidebarService.searchSidebars(request);

        ApiResponse<PageResponse<SidebarResponseDTO>> response =
                ApiResponse
                        .<PageResponse<SidebarResponseDTO>>builder()
                        .success(true)
                        .message("Sidebars retrieved successfully")
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get sidebar by ID",
            description = "Returns a sidebar section by UUID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Sidebar retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Sidebar not found"
            )
    })
    public ResponseEntity<ApiResponse<SidebarResponseDTO>>
    getSidebarById(
            @Parameter(
                    description = "Sidebar UUID",
                    required = true
            )
            @PathVariable UUID id
    ) {

        log.debug(
                "REST request to get sidebar. id={}",
                id
        );

        SidebarResponseDTO data =
                sidebarService.getSidebarById(id);

        ApiResponse<SidebarResponseDTO> response =
                ApiResponse.<SidebarResponseDTO>builder()
                        .success(true)
                        .message("Sidebar retrieved successfully")
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    @Operation(
            summary = "Update sidebar",
            description = "Updates an existing sidebar section"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Sidebar updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid sidebar data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Sidebar not found"
            )
    })
    public ResponseEntity<ApiResponse<SidebarResponseDTO>>
    updateSidebar(
            @Parameter(
                    description = "Sidebar UUID",
                    required = true
            )
            @PathVariable UUID id,

            @Valid @RequestBody UpdateSidebarDTO request
    ) {

        log.info(
                "REST request to update sidebar. id={}",
                id
        );

        SidebarResponseDTO data =
                sidebarService.updateSidebar(
                        id,
                        request
                );

        ApiResponse<SidebarResponseDTO> response =
                ApiResponse.<SidebarResponseDTO>builder()
                        .success(true)
                        .message("Sidebar updated successfully")
                        .data(data)
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ACTIVATE
    // =========================================================

    @PutMapping("/{id}/activate")
    @Operation(
            summary = "Activate sidebar",
            description = "Activates a sidebar section"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Sidebar activated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Sidebar not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>>
    activateSidebar(
            @Parameter(
                    description = "Sidebar UUID",
                    required = true
            )
            @PathVariable UUID id
    ) {

        log.info(
                "REST request to activate sidebar. id={}",
                id
        );

        sidebarService.activateSidebar(id);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Sidebar activated successfully")
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @PutMapping("/{id}/deactivate")
    @Operation(
            summary = "Deactivate sidebar",
            description = "Deactivates a sidebar section"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Sidebar deactivated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Sidebar not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>>
    deactivateSidebar(
            @Parameter(
                    description = "Sidebar UUID",
                    required = true
            )
            @PathVariable UUID id
    ) {

        log.info(
                "REST request to deactivate sidebar. id={}",
                id
        );

        sidebarService.deactivateSidebar(id);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Sidebar deactivated successfully")
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete sidebar",
            description = "Soft deletes a sidebar section"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Sidebar deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Sidebar not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>>
    deleteSidebar(
            @Parameter(
                    description = "Sidebar UUID",
                    required = true
            )
            @PathVariable UUID id
    ) {

        log.info(
                "REST request to delete sidebar. id={}",
                id
        );

        sidebarService.deleteSidebar(id);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Sidebar deleted successfully")
                        .build();

        return ResponseEntity.ok(response);
    }
}