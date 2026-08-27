//package com.esign.platform.common.audit.controller;
//
//import java.time.Instant;
//import java.util.UUID;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.esign.platform.common.audit.dto.AuditTrailResponse;
//import com.esign.platform.common.audit.dto.AuditTrailSearchRequest;
//import com.esign.platform.common.audit.entity.AuditTrail;
//import com.esign.platform.common.audit.repository.AuditTrailRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/api/v1/audit-trail")
//@RequiredArgsConstructor
//public class AuditTrailController {
//
//    private final AuditTrailRepository auditTrailRepository;
//
//    @GetMapping
//    public Page<AuditTrailResponse> search(
//            AuditTrailSearchRequest request
//    ) {
//
//        int page =
//                Math.max(
//                        request.getPage(),
//                        0
//                );
//
//        int size =
//                Math.min(
//                        Math.max(
//                                request.getSize(),
//                                1
//                        ),
//                        100
//                );
//
//        String sortBy = validateSortField(
//                        request.getSortBy()
//                );
//
//        Sort.Direction direction =
//                "ASC".equalsIgnoreCase(
//                        request.getSortDirection()
//                )
//                        ? Sort.Direction.ASC
//                        : Sort.Direction.DESC;
//
//        Pageable pageable = PageRequest.of(
//        		page,
//                size,
//                Sort.by(
//                        direction,
//                        sortBy
//                        )
//                );
//
//        Specification<AuditTrail> specification =
//                buildSpecification(request);
//
//        return auditTrailRepository
//                .findAll(
//                        specification,
//                        pageable
//                )
//                .map(
//                        AuditTrailResponse::from
//                );
//    }
//
//    private Specification<AuditTrail>
//    buildSpecification(
//            AuditTrailSearchRequest request
//    ) {
//
//        Specification<AuditTrail> specification =
//                Specification.where(null);
//
//        UUID organizationId =
//                request.getOrganizationId();
//
//        if (organizationId != null) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.equal(
//                                            root.get("organizationId"),
//                                            organizationId
//                                    )
//                    );
//        }
//
//        UUID userId = request.getUserId();
//
//        if (userId != null) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.equal(
//                                            root.get("userId"),
//                                            userId
//                                    )
//                    );
//        }
//
//        UUID employeeId =
//                request.getEmployeeId();
//
//        if (employeeId != null) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.equal(
//                                            root.get("employeeId"),
//                                            employeeId
//                                    )
//                    );
//        }
//
//        if (
//                request.getAction() != null
//                        &&
//                !request.getAction().isBlank()
//        ) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.equal(
//                                            root.get("action"),
//                                            request.getAction()
//                                    )
//                    );
//        }
//
//        if (
//                request.getEntityType() != null
//                        &&
//                !request.getEntityType().isBlank()
//        ) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.equal(
//                                            root.get("entityType"),
//                                            request.getEntityType()
//                                    )
//                    );
//        }
//
//        UUID entityId =
//                request.getEntityId();
//
//        if (entityId != null) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.equal(
//                                            root.get("entityId"),
//                                            entityId
//                                    )
//                    );
//        }
//
//        if (
//                request.getStatus() != null
//                        &&
//                !request.getStatus().isBlank()
//        ) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.equal(
//                                            root.get("status"),
//                                            request.getStatus()
//                                    )
//                    );
//        }
//
//        Instant from =
//                request.getFrom();
//
//        if (from != null) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.greaterThanOrEqualTo(
//                                            root.get("createdAt"),
//                                            from
//                                    )
//                    );
//        }
//
//        Instant to =
//                request.getTo();
//
//        if (to != null) {
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.lessThanOrEqualTo(
//                                            root.get("createdAt"),
//                                            to
//                                    )
//                    );
//        }
//
//        if (
//                request.getSearch() != null
//                        &&
//                !request.getSearch().isBlank()
//        ) {
//
//            String search =
//                    "%" +
//                    request.getSearch().toLowerCase() +
//                    "%";
//
//            specification =
//                    specification.and(
//                            (root, query, cb) ->
//                                    cb.or(
//
//                                            cb.like(
//                                                    cb.lower(
//                                                            root.get("action")
//                                                    ),
//                                                    search
//                                            ),
//
//                                            cb.like(
//                                                    cb.lower(
//                                                            root.get("entityType")
//                                                    ),
//                                                    search
//                                            ),
//
//                                            cb.like(
//                                                    cb.lower(
//                                                            root.get("description")
//                                                    ),
//                                                    search
//                                            ),
//
//                                            cb.like(
//                                                    cb.lower(
//                                                            root.get("requestUri")
//                                                    ),
//                                                    search
//                                            )
//                                    )
//                    );
//        }
//
//        return specification;
//    }
//
//    private String validateSortField(
//            String sortBy
//    ) {
//
//        if (sortBy == null) {
//            return "createdAt";
//        }
//
//        return switch (sortBy) {
//
//            case "createdAt" ->
//                    "createdAt";
//
//            case "action" ->
//                    "action";
//
//            case "entityType" ->
//                    "entityType";
//
//            case "status" ->
//                    "status";
//
//            default ->
//                    "createdAt";
//        };
//    }
//}