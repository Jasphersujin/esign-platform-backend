package com.esign.platform.organizationmanagement.department.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esign.platform.organizationmanagement.department.dto.req.DepartmentCreateRequest;
import com.esign.platform.organizationmanagement.department.dto.req.DepartmentUpdateRequest;
import com.esign.platform.organizationmanagement.department.dto.res.DepartmentResponse;
import com.esign.platform.organizationmanagement.department.entity.DepartmentEntity;
import com.esign.platform.organizationmanagement.department.repository.DepartmentRepository;
import com.esign.platform.organizationmanagement.department.service.DepartmentService;
import com.esign.platform.organizationmanagement.department.specification.DepartmentSpecification;
import com.esign.platform.organizationmanagement.organization.entity.OrganizationEntity;
import com.esign.platform.organizationmanagement.organization.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl
        implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final OrganizationRepository organizationRepository;


    /*
     * ============================================================
     * CREATE
     * ============================================================
     */

    @Override
    public DepartmentResponse create(
        DepartmentCreateRequest request
    ) {

        OrganizationEntity organization =
            organizationRepository
                .findById(
                    request.getOrganizationId()
                )
                .orElseThrow(
                    () -> new RuntimeException(
                        "Organization not found"
                    )
                );


        if (
            Boolean.TRUE.equals(
                organization.getDeleted()
            )
        ) {

            throw new RuntimeException(
                "Cannot create department for a deleted organization"
            );
        }


        boolean exists =
            departmentRepository
                .existsByOrganization_IdAndDepartmentCodeIgnoreCase(
                    request.getOrganizationId(),
                    request.getDepartmentCode().trim()
                );


        if (exists) {

            throw new RuntimeException(
                "Department code already exists for this organization"
            );
        }


        DepartmentEntity entity =
            new DepartmentEntity();


        entity.setOrganization(
            organization
        );


        entity.setDepartmentName(
            request.getDepartmentName().trim()
        );


        entity.setDepartmentCode(
            request.getDepartmentCode()
                .trim()
                .toUpperCase()
        );


        entity.setDepartmentType(
            trimToNull(
                request.getDepartmentType()
            )
        );


        entity.setDescription(
            trimToNull(
                request.getDescription()
            )
        );


        entity.setActive(
            request.getActive() != null
                ? request.getActive()
                : true
        );


        entity.setDeleted(false);


        DepartmentEntity saved =
            departmentRepository.save(
                entity
            );


        return toResponse(saved);
    }


    /*
     * ============================================================
     * GET BY ID
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getById(
        UUID id
    ) {

        DepartmentEntity entity =
            departmentRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Department not found"
                    )
                );


        return toResponse(entity);
    }


    /*
     * ============================================================
     * UPDATE
     * ============================================================
     */

    @Override
    public DepartmentResponse update(
        UUID id,
        DepartmentUpdateRequest request
    ) {

        DepartmentEntity entity =
            departmentRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Department not found"
                    )
                );


        OrganizationEntity organization =
            organizationRepository
                .findById(
                    request.getOrganizationId()
                )
                .orElseThrow(
                    () -> new RuntimeException(
                        "Organization not found"
                    )
                );


        if (
            Boolean.TRUE.equals(
                organization.getDeleted()
            )
        ) {

            throw new RuntimeException(
                "Cannot assign department to a deleted organization"
            );
        }


        boolean exists =
            departmentRepository
                .existsByOrganization_IdAndDepartmentCodeIgnoreCaseAndIdNot(
                    request.getOrganizationId(),
                    request.getDepartmentCode().trim(),
                    id
                );


        if (exists) {

            throw new RuntimeException(
                "Department code already exists for this organization"
            );
        }


        entity.setOrganization(
            organization
        );


        entity.setDepartmentName(
            request.getDepartmentName().trim()
        );


        entity.setDepartmentCode(
            request.getDepartmentCode()
                .trim()
                .toUpperCase()
        );


        entity.setDepartmentType(
            trimToNull(
                request.getDepartmentType()
            )
        );


        entity.setDescription(
            trimToNull(
                request.getDescription()
            )
        );


        if (
            request.getActive() != null
        ) {

            entity.setActive(
                request.getActive()
            );
        }


        DepartmentEntity updated =
            departmentRepository.save(
                entity
            );


        return toResponse(updated);
    }


    /*
     * ============================================================
     * SOFT DELETE
     * ============================================================
     */

    @Override
    public void delete(
        UUID id
    ) {

        DepartmentEntity entity =
            departmentRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Department not found"
                    )
                );


        if (
            Boolean.TRUE.equals(
                entity.getDeleted()
            )
        ) {

            throw new RuntimeException(
                "Department is already deleted"
            );
        }


        entity.setDeleted(true);

        entity.setActive(false);


        departmentRepository.save(entity);
    }


    /*
     * ============================================================
     * RESTORE
     * ============================================================
     */

    @Override
    public void restore(
        UUID id
    ) {

        DepartmentEntity entity =
            departmentRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Department not found"
                    )
                );


        if (
            !Boolean.TRUE.equals(
                entity.getDeleted()
            )
        ) {

            throw new RuntimeException(
                "Department is not deleted"
            );
        }


        if (
            entity.getOrganization() == null ||
            Boolean.TRUE.equals(
                entity
                    .getOrganization()
                    .getDeleted()
            )
        ) {

            throw new RuntimeException(
                "Cannot restore department because organization is deleted"
            );
        }


        entity.setDeleted(false);

        entity.setActive(true);


        departmentRepository.save(entity);
    }


    /*
     * ============================================================
     * GET BY ORGANIZATION
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getByOrganization(
        UUID organizationId
    ) {

        OrganizationEntity organization =
            organizationRepository
                .findById(
                    organizationId
                )
                .orElseThrow(
                    () -> new RuntimeException(
                        "Organization not found"
                    )
                );


        if (
            Boolean.TRUE.equals(
                organization.getDeleted()
            )
        ) {

            throw new RuntimeException(
                "Organization not found"
            );
        }


        return departmentRepository
            .findByOrganization_IdAndDeletedFalse(
                organizationId
            )
            .stream()
            .map(this::toResponse)
            .toList();
    }


    /*
     * ============================================================
     * SEARCH
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> search(

        UUID organizationId,

        String search,

        String departmentName,

        String departmentCode,

        String departmentType,

        Boolean active,

        Boolean deleted,

        Boolean includeDeleted,

        LocalDate createdFrom,

        LocalDate createdTo,

        LocalDate updatedFrom,

        LocalDate updatedTo,

        int page,

        int size,

        String sortBy,

        String sortDirection

    ) {

        /*
         * --------------------------------------------------------
         * PAGE VALIDATION
         * --------------------------------------------------------
         */

        if (page < 0) {
            page = 0;
        }


        if (size <= 0) {
            size = 20;
        }


        /*
         * Prevent unnecessarily large requests.
         */

        if (size > 100) {
            size = 100;
        }


        /*
         * --------------------------------------------------------
         * SORT VALIDATION
         * --------------------------------------------------------
         */

        if (
            sortBy == null ||
            sortBy.isBlank()
        ) {

            sortBy = "createdAt";
        }


        /*
         * Only allow known sortable fields.
         *
         * This also prevents someone from passing arbitrary
         * property names to Spring Data.
         */

        sortBy =
            normalizeSortField(sortBy);


        Sort.Direction direction =
            "ASC".equalsIgnoreCase(
                sortDirection
            )
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;


        Pageable pageable =
            PageRequest.of(
                page,
                size,
                Sort.by(
                    direction,
                    sortBy
                )
            );


        /*
         * --------------------------------------------------------
         * BUILD SPECIFICATION
         * --------------------------------------------------------
         */

//        Specification<DepartmentEntity>
//            specification =
//                Specification.where(null);
        
        Specification<DepartmentEntity>
        specification =
            Specification.unrestricted();


        /*
         * Organization
         */

        specification =
            specification.and(
                DepartmentSpecification
                    .hasOrganizationId(
                        organizationId
                    )
            );


        /*
         * Global search
         */

        specification =
            specification.and(
                DepartmentSpecification
                    .search(search)
            );


        /*
         * Department name
         */

        specification =
            specification.and(
                DepartmentSpecification
                    .hasDepartmentName(
                        departmentName
                    )
            );


        /*
         * Department code
         */

        specification =
            specification.and(
                DepartmentSpecification
                    .hasDepartmentCode(
                        departmentCode
                    )
            );


        /*
         * Department type
         */

        specification =
            specification.and(
                DepartmentSpecification
                    .hasDepartmentType(
                        departmentType
                    )
            );


        /*
         * Active / inactive
         */

        specification =
            specification.and(
                DepartmentSpecification
                    .hasActive(
                        active
                    )
            );


        /*
         * --------------------------------------------------------
         * DELETED FILTER
         * --------------------------------------------------------
         *
         * Default:
         *
         * deleted = false
         *
         * If includeDeleted=true:
         *
         * deleted filter is only applied if explicitly supplied.
         *
         */

        if (
            Boolean.TRUE.equals(
                includeDeleted
            )
        ) {

            if (deleted != null) {

                specification =
                    specification.and(
                        DepartmentSpecification
                            .hasDeleted(
                                deleted
                            )
                    );
            }

        } else {

            specification =
                specification.and(
                    DepartmentSpecification
                        .hasDeleted(false)
                );
        }


        /*
         * --------------------------------------------------------
         * CREATED DATE
         * --------------------------------------------------------
         */

        LocalDateTime createdFromDateTime =
            createdFrom == null
                ? null
                : createdFrom.atStartOfDay();


        LocalDateTime createdToDateTime =
            createdTo == null
                ? null
                : createdTo.atTime(
                    LocalTime.MAX
                );


        specification =
            specification.and(
                DepartmentSpecification
                    .createdFrom(
                        createdFromDateTime
                    )
            );


        specification =
            specification.and(
                DepartmentSpecification
                    .createdTo(
                        createdToDateTime
                    )
            );


        /*
         * --------------------------------------------------------
         * UPDATED DATE
         * --------------------------------------------------------
         */

        LocalDateTime updatedFromDateTime =
            updatedFrom == null
                ? null
                : updatedFrom.atStartOfDay();


        LocalDateTime updatedToDateTime =
            updatedTo == null
                ? null
                : updatedTo.atTime(
                    LocalTime.MAX
                );


        specification =
            specification.and(
                DepartmentSpecification
                    .updatedFrom(
                        updatedFromDateTime
                    )
            );


        specification =
            specification.and(
                DepartmentSpecification
                    .updatedTo(
                        updatedToDateTime
                    )
            );


        /*
         * --------------------------------------------------------
         * EXECUTE QUERY
         * --------------------------------------------------------
         */

        Page<DepartmentEntity> result =
            departmentRepository.findAll(
                specification,
                pageable
            );


        return result.map(
            this::toResponse
        );
    }


    /*
     * ============================================================
     * RESPONSE MAPPER
     * ============================================================
     */

    private DepartmentResponse toResponse(
        DepartmentEntity entity
    ) {

        DepartmentResponse response =
            new DepartmentResponse();


        response.setId(
            entity.getId()
        );


        if (
            entity.getOrganization() != null
        ) {

            response.setOrganizationId(
                entity
                    .getOrganization()
                    .getId()
            );


            response.setOrganizationName(
                entity
                    .getOrganization()
                    .getOrgName()
            );
        }


        response.setDepartmentName(
            entity.getDepartmentName()
        );


        response.setDepartmentCode(
            entity.getDepartmentCode()
        );


        response.setDepartmentType(
            entity.getDepartmentType()
        );


        response.setDescription(
            entity.getDescription()
        );


        response.setActive(
            entity.getActive()
        );


        response.setDeleted(
            entity.getDeleted()
        );


        response.setCreatedAt(
            entity.getCreatedAt()
        );


        response.setUpdatedAt(
            entity.getUpdatedAt()
        );


        response.setVersion(
            entity.getVersion()
        );


        return response;
    }


    /*
     * ============================================================
     * SORT FIELD WHITELIST
     * ============================================================
     */

    private String normalizeSortField(
        String sortBy
    ) {

        return switch (
            sortBy
                .trim()
                .toLowerCase()
        ) {

            case "name",
                 "departmentname" ->
                "departmentName";

            case "code",
                 "departmentcode" ->
                "departmentCode";

            case "type",
                 "departmenttype" ->
                "departmentType";

            case "description" ->
                "description";

            case "active" ->
                "active";

            case "createdat",
                 "created" ->
                "createdAt";

            case "updatedat",
                 "updated" ->
                "updatedAt";

            default ->
                "createdAt";
        };
    }


    /*
     * ============================================================
     * STRING HELPER
     * ============================================================
     */

    private String trimToNull(
        String value
    ) {

        if (
            value == null ||
            value.isBlank()
        ) {

            return null;
        }

        return value.trim();
    }
}