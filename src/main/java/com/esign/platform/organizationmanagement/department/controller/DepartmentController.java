package com.esign.platform.organizationmanagement.department.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esign.platform.organizationmanagement.department.dto.req.DepartmentCreateRequest;
import com.esign.platform.organizationmanagement.department.dto.req.DepartmentUpdateRequest;
import com.esign.platform.organizationmanagement.department.dto.res.DepartmentResponse;
import com.esign.platform.organizationmanagement.department.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;


    /*
     * ============================================================
     * CREATE
     * ============================================================
     */

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(
        @Valid
        @RequestBody
        DepartmentCreateRequest request
    ) {

        DepartmentResponse response =
            departmentService.create(
                request
            );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }


    /*
     * ============================================================
     * GET BY ID
     * ============================================================
     */

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(
        @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
            departmentService.getById(id)
        );
    }


    /*
     * ============================================================
     * GET BY ORGANIZATION
     * ============================================================
     *
     * GET
     * /api/v1/departments/organization/{organizationId}
     *
     */

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<DepartmentResponse>>
    getByOrganization(
        @PathVariable UUID organizationId
    ) {

        return ResponseEntity.ok(
            departmentService.getByOrganization(
                organizationId
            )
        );
    }


    /*
     * ============================================================
     * UPDATE
     * ============================================================
     */

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(
        @PathVariable UUID id,

        @Valid
        @RequestBody
        DepartmentUpdateRequest request
    ) {

        return ResponseEntity.ok(
            departmentService.update(
                id,
                request
            )
        );
    }


    /*
     * ============================================================
     * SOFT DELETE
     * ============================================================
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable UUID id
    ) {

        departmentService.delete(id);

        return ResponseEntity
            .noContent()
            .build();
    }


    /*
     * ============================================================
     * RESTORE
     * ============================================================
     */

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(
        @PathVariable UUID id
    ) {

        departmentService.restore(id);

        return ResponseEntity
            .ok()
            .build();
    }


    /*
     * ============================================================
     * SEARCH / FILTER / SORT / PAGINATION
     * ============================================================
     */

    @GetMapping("/search")
    public ResponseEntity<Page<DepartmentResponse>> search(

        /*
         * Organization
         */

        @RequestParam(
            required = false
        )
        UUID organizationId,


        /*
         * Global search
         */

        @RequestParam(
            required = false
        )
        String search,


        /*
         * Department name
         */

        @RequestParam(
            required = false
        )
        String departmentName,


        /*
         * Department code
         */

        @RequestParam(
            required = false
        )
        String departmentCode,


        /*
         * Department type
         */

        @RequestParam(
            required = false
        )
        String departmentType,


        /*
         * Active / inactive
         */

        @RequestParam(
            required = false
        )
        Boolean active,


        /*
         * Deleted
         */

        @RequestParam(
            required = false
        )
        Boolean deleted,


        /*
         * Include deleted
         */

        @RequestParam(
            defaultValue = "false"
        )
        Boolean includeDeleted,


        /*
         * Created date range
         */

        @RequestParam(
            required = false
        )
        LocalDate createdFrom,


        @RequestParam(
            required = false
        )
        LocalDate createdTo,


        /*
         * Updated date range
         */

        @RequestParam(
            required = false
        )
        LocalDate updatedFrom,


        @RequestParam(
            required = false
        )
        LocalDate updatedTo,


        /*
         * Pagination
         */

        @RequestParam(
            defaultValue = "0"
        )
        int page,


        @RequestParam(
            defaultValue = "20"
        )
        int size,


        /*
         * Sorting
         */

        @RequestParam(
            defaultValue = "createdAt"
        )
        String sortBy,


        @RequestParam(
            defaultValue = "DESC"
        )
        String sortDirection

    ) {

        Page<DepartmentResponse> response =
            departmentService.search(

                organizationId,

                search,

                departmentName,

                departmentCode,

                departmentType,

                active,

                deleted,

                includeDeleted,

                createdFrom,

                createdTo,

                updatedFrom,

                updatedTo,

                page,

                size,

                sortBy,

                sortDirection
            );


        return ResponseEntity.ok(
            response
        );
    }
}