package com.esign.platform.system.menu.serviceimpl;

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
import com.esign.platform.system.menu.dto.CreateMenuDTO;
import com.esign.platform.system.menu.dto.MenuResponseDTO;
import com.esign.platform.system.menu.dto.MenuSearchDTO;
import com.esign.platform.system.menu.dto.UpdateMenuDTO;
import com.esign.platform.system.menu.entity.MenuEntity;
import com.esign.platform.system.menu.repository.MenuRepository;
import com.esign.platform.system.menu.service.MenuService;
import com.esign.platform.system.sidebar.entity.SidebarEntity;
import com.esign.platform.system.sidebar.repository.SidebarRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final SidebarRepository sidebarRepository;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "menuName",
            "displayOrder",
            "createdAt",
            "updatedAt"
    );

    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public MenuResponseDTO createMenu(
            CreateMenuDTO request
    ) {

        log.info(
                "Creating menu. menuName={}, sidebarId={}",
                request.getMenuName(),
                request.getSidebarId()
        );

        String menuName = request.getMenuName().trim();

        validateSidebar(request.getSidebarId());

        validateDuplicateMenu(
                menuName,
                request.getSidebarId(),
                null
        );

        MenuEntity menu = MenuEntity.builder()
                .menuName(menuName)
                .sidebarId(request.getSidebarId())
                .description(request.getDescription())
                .icon(request.getIcon())
                .displayOrder(request.getDisplayOrder())
                .build();

        MenuEntity savedMenu =
                menuRepository.save(menu);

        log.info(
                "Menu created successfully. id={}",
                savedMenu.getId()
        );

        return mapToResponse(savedMenu);
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public MenuResponseDTO getMenuById(UUID id) {

        log.debug(
                "Fetching menu. id={}",
                id
        );

        MenuEntity menu = findMenu(id);

        return mapToResponse(menu);
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<MenuResponseDTO> getAllMenus() {

        log.debug(
                "Fetching all non-deleted menus"
        );

        return menuRepository.findAll(
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
    public PageResponse<MenuResponseDTO> searchMenus(
            MenuSearchDTO request
    ) {

        log.info(
                "Searching menus. search={}, active={}, sidebarId={}, " +
                "standalone={}, page={}, size={}, sortBy={}, sortDirection={}",
                request.getSearch(),
                request.getActive(),
                request.getSidebarId(),
                request.getStandalone(),
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDirection()
        );

        String sortBy =
                resolveSortField(request.getSortBy());

        Sort.Direction direction =
                "DESC".equalsIgnoreCase(
                        request.getSortDirection()
                )
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        request.getPage(),
                        request.getSize(),
                        Sort.by(direction, sortBy)
                );

        Specification<MenuEntity> specification =
                buildSearchSpecification(request);

        Page<MenuEntity> page =
                menuRepository.findAll(
                        specification,
                        pageable
                );

        List<MenuResponseDTO> content =
                page.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        log.info(
                "Menu search completed. totalElements={}, totalPages={}",
                page.getTotalElements(),
                page.getTotalPages()
        );

        return PageResponse.<MenuResponseDTO>builder()
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
    public MenuResponseDTO updateMenu(
            UUID id,
            UpdateMenuDTO request
    ) {

        log.info(
                "Updating menu. id={}",
                id
        );

        MenuEntity menu = findMenu(id);

        String menuName =
                request.getMenuName().trim();

        validateSidebar(
                request.getSidebarId()
        );

        validateDuplicateMenu(
                menuName,
                request.getSidebarId(),
                id
        );

        menu.setMenuName(menuName);

        menu.setSidebarId(
                request.getSidebarId()
        );

        menu.setDescription(
                request.getDescription()
        );

        menu.setIcon(
                request.getIcon()
        );

        menu.setDisplayOrder(
                request.getDisplayOrder()
        );

        MenuEntity updatedMenu =
                menuRepository.save(menu);

        log.info(
                "Menu updated successfully. id={}",
                id
        );

        return mapToResponse(updatedMenu);
    }

    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public void activateMenu(UUID id) {

        log.info(
                "Activating menu. id={}",
                id
        );

        MenuEntity menu = findMenu(id);

        if (Boolean.TRUE.equals(menu.getActive())) {

            log.debug(
                    "Menu is already active. id={}",
                    id
            );

            return;
        }

        menu.setActive(true);

        menuRepository.save(menu);

        log.info(
                "Menu activated successfully. id={}",
                id
        );
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public void deactivateMenu(UUID id) {

        log.info(
                "Deactivating menu. id={}",
                id
        );

        MenuEntity menu = findMenu(id);

        if (Boolean.FALSE.equals(menu.getActive())) {

            log.debug(
                    "Menu is already inactive. id={}",
                    id
            );

            return;
        }

        menu.setActive(false);

        menuRepository.save(menu);

        log.info(
                "Menu deactivated successfully. id={}",
                id
        );
    }

    // =========================================================
    // SOFT DELETE
    // =========================================================

    @Override
    public void deleteMenu(UUID id) {

        log.info(
                "Soft deleting menu. id={}",
                id
        );

        MenuEntity menu = findMenu(id);

        menu.setDeleted(true);
        menu.setActive(false);

        menuRepository.save(menu);

        log.info(
                "Menu soft deleted successfully. id={}",
                id
        );
    }

    // =========================================================
    // FIND MENU
    // =========================================================

    private MenuEntity findMenu(UUID id) {

        return menuRepository.findById(id)
                .filter(menu ->
                        !Boolean.TRUE.equals(
                                menu.getDeleted()
                        )
                )
                .orElseThrow(() -> {

                    log.warn(
                            "Menu not found. id={}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Menu not found with id: " + id
                    );
                });
    }

    // =========================================================
    // SIDEBAR VALIDATION
    // =========================================================

    private void validateSidebar(UUID sidebarId) {

        if (sidebarId == null) {
            return;
        }

        SidebarEntity sidebar =
                sidebarRepository.findById(sidebarId)
                        .filter(entity ->
                                !Boolean.TRUE.equals(
                                        entity.getDeleted()
                                )
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sidebar not found with id: "
                                                + sidebarId
                                )
                        );

        if (!Boolean.TRUE.equals(
                sidebar.getActive()
        )) {

            throw new IllegalArgumentException(
                    "Cannot assign menu to an inactive sidebar"
            );
        }
    }

    // =========================================================
    // DUPLICATE VALIDATION
    // =========================================================

    private void validateDuplicateMenu(
            String menuName,
            UUID sidebarId,
            UUID currentMenuId
    ) {

        boolean exists;

        if (sidebarId == null) {

            exists =
                    menuRepository
                            .existsByMenuNameIgnoreCaseAndSidebarIdIsNullAndDeletedFalse(
                                    menuName
                            );

        } else {

            exists =
                    menuRepository
                            .existsByMenuNameIgnoreCaseAndSidebarIdAndDeletedFalse(
                                    menuName,
                                    sidebarId
                            );
        }

        if (!exists) {
            return;
        }

        /*
         * During update, ignore the current menu.
         */
        if (currentMenuId != null) {

            MenuEntity existing =
                    findDuplicateMenu(
                            menuName,
                            sidebarId
                    );

            if (existing != null &&
                    existing.getId().equals(
                            currentMenuId
                    )) {

                return;
            }
        }

        log.warn(
                "Duplicate menu detected. menuName={}, sidebarId={}",
                menuName,
                sidebarId
        );

        throw new IllegalArgumentException(
                "Menu with name '" +
                        menuName +
                        "' already exists in the selected scope"
        );
    }

    private MenuEntity findDuplicateMenu(
            String menuName,
            UUID sidebarId
    ) {

        return menuRepository.findAll()
                .stream()
                .filter(menu ->
                        !Boolean.TRUE.equals(
                                menu.getDeleted()
                        )
                )
                .filter(menu ->
                        menu.getMenuName()
                                .equalsIgnoreCase(menuName)
                )
                .filter(menu -> {

                    if (sidebarId == null) {
                        return menu.getSidebarId() == null;
                    }

                    return sidebarId.equals(
                            menu.getSidebarId()
                    );
                })
                .findFirst()
                .orElse(null);
    }

    // =========================================================
    // SEARCH SPECIFICATION
    // =========================================================

    private Specification<MenuEntity>
    buildSearchSpecification(
            MenuSearchDTO request
    ) {

        return (root, query, criteriaBuilder) -> {

            var predicates =
                    criteriaBuilder.conjunction();

            // Never return soft deleted records
            predicates.getExpressions().add(
                    criteriaBuilder.equal(
                            root.get("deleted"),
                            false
                    )
            );

            // Search by menu name
            if (request.getSearch() != null &&
                    !request.getSearch()
                            .trim()
                            .isEmpty()) {

                String search =
                        "%" +
                        request.getSearch()
                                .trim()
                                .toLowerCase() +
                        "%";

                predicates.getExpressions().add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("menuName")
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

            // Sidebar filter
            if (request.getSidebarId() != null) {

                predicates.getExpressions().add(
                        criteriaBuilder.equal(
                                root.get("sidebarId"),
                                request.getSidebarId()
                        )
                );
            }

            // Standalone filter
            if (request.getStandalone() != null) {

                if (Boolean.TRUE.equals(
                        request.getStandalone()
                )) {

                    predicates.getExpressions().add(
                            criteriaBuilder.isNull(
                                    root.get("sidebarId")
                            )
                    );

                } else {

                    predicates.getExpressions().add(
                            criteriaBuilder.isNotNull(
                                    root.get("sidebarId")
                            )
                    );
                }
            }

            return predicates;
        };
    }

    // =========================================================
    // SORT VALIDATION
    // =========================================================

    private String resolveSortField(
            String sortBy
    ) {

        if (sortBy == null ||
                sortBy.trim().isEmpty()) {

            return "displayOrder";
        }

        if (!ALLOWED_SORT_FIELDS.contains(
                sortBy
        )) {

            log.warn(
                    "Invalid menu sort field '{}'. " +
                    "Using displayOrder.",
                    sortBy
            );

            return "displayOrder";
        }

        return sortBy;
    }

    // =========================================================
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private MenuResponseDTO mapToResponse(
            MenuEntity entity
    ) {

        return MenuResponseDTO.builder()

                .id(entity.getId())

                .menuName(
                        entity.getMenuName()
                )

                .sidebarId(
                        entity.getSidebarId()
                )

                .description(
                        entity.getDescription()
                )

                .icon(
                        entity.getIcon()
                )

                .displayOrder(
                        entity.getDisplayOrder()
                )

                .active(
                        entity.getActive()
                )

                .deleted(
                        entity.getDeleted()
                )

                .createdAt(
                        entity.getCreatedAt()
                )

                .createdBy(
                        entity.getCreatedBy()
                )

                .updatedAt(
                        entity.getUpdatedAt()
                )

                .updatedBy(
                        entity.getUpdatedBy()
                )

                .deletedAt(
                        entity.getDeletedAt()
                )

                .deletedBy(
                        entity.getDeletedBy()
                )

                .version(
                        entity.getVersion()
                )

                .build();
    }
}