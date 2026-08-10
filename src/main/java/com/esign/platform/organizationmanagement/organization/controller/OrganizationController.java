package com.esign.platform.organizationmanagement.organization.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.esign.platform.organizationmanagement.organization.dto.req.OrganizationCreateRequest;
import com.esign.platform.organizationmanagement.organization.dto.req.OrganizationUpdateRequest;
import com.esign.platform.organizationmanagement.organization.dto.res.OrganizationResponse;
import com.esign.platform.organizationmanagement.organization.service.OrganizationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<OrganizationResponse> create(
            @Valid @RequestBody OrganizationCreateRequest request) {

        OrganizationResponse response =
                organizationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                organizationService.getById(id)
        );
    }


    // =========================================================
    // LIST
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<OrganizationResponse>> getAll(
            @RequestParam(required = false) String search,

            @PageableDefault(
                    page = 0,
                    size = 20
            )
            Pageable pageable) {

        return ResponseEntity.ok(
                organizationService.getAll(
                        search,
                        pageable
                )
        );
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody OrganizationUpdateRequest request) {

        return ResponseEntity.ok(
                organizationService.update(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // SOFT DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        organizationService.softDelete(id);

        return ResponseEntity.noContent().build();
    }
}