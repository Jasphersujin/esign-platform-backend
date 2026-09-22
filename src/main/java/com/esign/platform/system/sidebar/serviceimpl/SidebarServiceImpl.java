package com.esign.platform.system.sidebar.serviceimpl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esign.platform.common.dto.PageResponse;
import com.esign.platform.common.exception.ResourceNotFoundException;
import com.esign.platform.system.sidebar.dto.CreateSidebarDTO;
import com.esign.platform.system.sidebar.dto.SidebarResponseDTO;
import com.esign.platform.system.sidebar.dto.SidebarSearchDTO;
import com.esign.platform.system.sidebar.dto.UpdateSidebarDTO;
import com.esign.platform.system.sidebar.entity.SidebarEntity;
import com.esign.platform.system.sidebar.repository.SidebarRepository;
import com.esign.platform.system.sidebar.service.SidebarService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SidebarServiceImpl implements SidebarService {

    private final SidebarRepository sidebarRepository;

    /*
     * Allowed fields for sorting.
     * This prevents invalid/untrusted property names
     * from being passed directly to Spring Data.
     */
    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "displayName",
            "displayOrder",
            "createdAt",
            "updatedAt"
    );

    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public SidebarResponseDTO createSidebar(
            CreateSidebarDTO request
    ) {

        log.info(
                "Creating sidebar with displayName={}",
                request.getDisplayName()
        );

        boolean exists =
                sidebarRepository
                        .existsByDisplayNameIgnoreCaseAndDeletedFalse(
                                request.getDisplayName()
                        );

        if (exists) {

            log.warn(
                    "Sidebar creation failed. Display name already exists: {}",
                    request.getDisplayName()
            );

            throw new IllegalArgumentException(
                    "Sidebar with display name '"
                            + request.getDisplayName()
                            + "' already exists"
            );
        }

        SidebarEntity sidebar = SidebarEntity.builder()
                .displayName(request.getDisplayName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .displayOrder(request.getDisplayOrder())
                .build();

        SidebarEntity savedSidebar =
                sidebarRepository.save(sidebar);

        log.info(
                "Sidebar created successfully. id={}",
                savedSidebar.getId()
        );

        return mapToResponse(savedSidebar);
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SidebarResponseDTO getSidebarById(UUID id) {

        log.debug(
                "Fetching sidebar with id={}",
                id
        );

        SidebarEntity sidebar = findSidebar(id);

        return mapToResponse(sidebar);
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SidebarResponseDTO> getAllSidebars() {

        log.debug("Fetching all non-deleted sidebars");

        return sidebarRepository.findAll(
                        (root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(
                                        root.get("deleted"),
                                        false
                                ),
                        Sort.by(
                                Sort.Direction.ASC,
                                "displayOrder"
                        )
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // SEARCH + FILTER + PAGINATION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SidebarResponseDTO> searchSidebars(
            SidebarSearchDTO request
    ) {

        log.info(
                "Searching sidebars. search={}, active={}, page={}, size={}, sortBy={}, sortDirection={}",
                request.getSearch(),
                request.getActive(),
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDirection()
        );

        String sortBy = resolveSortField(
                request.getSortBy()
        );

        Sort.Direction direction =
                "DESC".equalsIgnoreCase(
                        request.getSortDirection()
                )
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(direction, sortBy)
        );

        Specification<SidebarEntity> specification =
                buildSearchSpecification(request);

        Page<SidebarEntity> page =
                sidebarRepository.findAll(
                        specification,
                        pageable
                );

        List<SidebarResponseDTO> content =
                page.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        log.info(
                "Sidebar search completed. totalElements={}, totalPages={}",
                page.getTotalElements(),
                page.getTotalPages()
        );

        return PageResponse.<SidebarResponseDTO>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public SidebarResponseDTO updateSidebar(
            UUID id,
            UpdateSidebarDTO request
    ) {

        log.info(
                "Updating sidebar with id={}",
                id
        );

        SidebarEntity sidebar = findSidebar(id);

        boolean nameChanged =
                !sidebar.getDisplayName()
                        .equalsIgnoreCase(
                                request.getDisplayName()
                        );

        if (nameChanged &&
                sidebarRepository
                        .existsByDisplayNameIgnoreCaseAndDeletedFalse(
                                request.getDisplayName()
                        )) {

            log.warn(
                    "Sidebar update failed. Display name already exists: {}",
                    request.getDisplayName()
            );

            throw new IllegalArgumentException(
                    "Sidebar with display name '"
                            + request.getDisplayName()
                            + "' already exists"
            );
        }

        sidebar.setDisplayName(
                request.getDisplayName()
        );

        sidebar.setDescription(
                request.getDescription()
        );

        sidebar.setIcon(
                request.getIcon()
        );

        sidebar.setDisplayOrder(
                request.getDisplayOrder()
        );

        SidebarEntity updatedSidebar =
                sidebarRepository.save(sidebar);

        log.info(
                "Sidebar updated successfully. id={}",
                id
        );

        return mapToResponse(updatedSidebar);
    }

    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public void activateSidebar(UUID id) {

        log.info(
                "Activating sidebar with id={}",
                id
        );

        SidebarEntity sidebar = findSidebar(id);

        if (Boolean.TRUE.equals(sidebar.getActive())) {

            log.debug(
                    "Sidebar is already active. id={}",
                    id
            );

            return;
        }

        sidebar.setActive(true);

        sidebarRepository.save(sidebar);

        log.info(
                "Sidebar activated successfully. id={}",
                id
        );
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public void deactivateSidebar(UUID id) {

        log.info(
                "Deactivating sidebar with id={}",
                id
        );

        SidebarEntity sidebar = findSidebar(id);

        if (Boolean.FALSE.equals(sidebar.getActive())) {

            log.debug(
                    "Sidebar is already inactive. id={}",
                    id
            );

            return;
        }

        sidebar.setActive(false);

        sidebarRepository.save(sidebar);

        log.info(
                "Sidebar deactivated successfully. id={}",
                id
        );
    }

    // =========================================================
    // SOFT DELETE
    // =========================================================

    @Override
    public void deleteSidebar(UUID id) {

        log.info(
                "Soft deleting sidebar with id={}",
                id
        );

        SidebarEntity sidebar = findSidebar(id);

        sidebar.setDeleted(true);
        sidebar.setActive(false);

        sidebarRepository.save(sidebar);

        log.info(
                "Sidebar soft deleted successfully. id={}",
                id
        );
    }

    // =========================================================
    // FIND SIDEBAR
    // =========================================================

    private SidebarEntity findSidebar(UUID id) {

        return sidebarRepository.findById(id)
                .filter(sidebar ->
                        !Boolean.TRUE.equals(
                                sidebar.getDeleted()
                        )
                )
                .orElseThrow(() -> {

                    log.warn(
                            "Sidebar not found. id={}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Sidebar not found with id: " + id
                    );
                });
    }

    // =========================================================
    // SEARCH SPECIFICATION
    // =========================================================

    private Specification<SidebarEntity> buildSearchSpecification(
            SidebarSearchDTO request
    ) {

        return (root, query, criteriaBuilder) -> {

            var predicates =
                    criteriaBuilder.conjunction();

            // Never return soft-deleted records
            predicates.getExpressions().add(
                    criteriaBuilder.equal(
                            root.get("deleted"),
                            false
                    )
            );

            // Search by display name
            if (request.getSearch() != null &&
                    !request.getSearch().trim().isEmpty()) {

                String search =
                        "%" +
                        request.getSearch()
                                .trim()
                                .toLowerCase() +
                        "%";

                predicates.getExpressions().add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("displayName")
                                ),
                                search
                        )
                );
            }

            // Active filter
            if (request.getActive() != null) {

                predicates.getExpressions().add(
                        criteriaBuilder.equal(
                                root.get("active"),
                                request.getActive()
                        )
                );
            }

            return predicates;
        };
    }

    // =========================================================
    // SORT VALIDATION
    // =========================================================

    private String resolveSortField(String sortBy) {

        if (sortBy == null ||
                sortBy.trim().isEmpty()) {

            return "displayOrder";
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {

            log.warn(
                    "Invalid sidebar sort field '{}'. Using displayOrder.",
                    sortBy
            );

            return "displayOrder";
        }

        return sortBy;
    }

    // =========================================================
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private SidebarResponseDTO mapToResponse(
            SidebarEntity entity
    ) {

        return SidebarResponseDTO.builder()
                .id(entity.getId())
                .displayName(entity.getDisplayName())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .displayOrder(entity.getDisplayOrder())
                .active(entity.getActive())
                .deleted(entity.getDeleted())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .version(entity.getVersion())
                .build();
    }
}