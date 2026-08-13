package com.esign.platform.organizationmanagement.department.specification;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.esign.platform.organizationmanagement.department.entity.DepartmentEntity;

public final class DepartmentSpecification {

    private DepartmentSpecification() {
    }

    /*
     * ============================================================
     * ORGANIZATION
     * ============================================================
     */

    public static Specification<DepartmentEntity> hasOrganizationId(
        UUID organizationId
    ) {

        return (root, query, cb) -> {

            if (organizationId == null) {
                return null;
            }

            return cb.equal(
                root.get("organization").get("id"),
                organizationId
            );
        };
    }

    /*
     * ============================================================
     * GLOBAL SEARCH
     *
     * Searches:
     * - department name
     * - department code
     * - department type
     * - description
     * ============================================================
     */

    public static Specification<DepartmentEntity> search(
        String search
    ) {

        return (root, query, cb) -> {

            if (
                search == null ||
                search.isBlank()
            ) {
                return null;
            }

            String value =
                "%" +
                search.trim().toLowerCase() +
                "%";

            return cb.or(

                cb.like(
                    cb.lower(
                        root.get("departmentName")
                    ),
                    value
                ),

                cb.like(
                    cb.lower(
                        root.get("departmentCode")
                    ),
                    value
                ),

                cb.like(
                    cb.lower(
                        root.get("departmentType")
                    ),
                    value
                ),

                cb.like(
                    cb.lower(
                        root.get("description")
                    ),
                    value
                )
            );
        };
    }

    /*
     * ============================================================
     * DEPARTMENT NAME
     * ============================================================
     */

    public static Specification<DepartmentEntity> hasDepartmentName(
        String departmentName
    ) {

        return (root, query, cb) -> {

            if (
                departmentName == null ||
                departmentName.isBlank()
            ) {
                return null;
            }

            return cb.like(
                cb.lower(
                    root.get("departmentName")
                ),
                "%" +
                departmentName.trim().toLowerCase() +
                "%"
            );
        };
    }

    /*
     * ============================================================
     * DEPARTMENT CODE
     * ============================================================
     */

    public static Specification<DepartmentEntity> hasDepartmentCode(
        String departmentCode
    ) {

        return (root, query, cb) -> {

            if (
                departmentCode == null ||
                departmentCode.isBlank()
            ) {
                return null;
            }

            return cb.equal(
                cb.lower(
                    root.get("departmentCode")
                ),
                departmentCode.trim().toLowerCase()
            );
        };
    }

    /*
     * ============================================================
     * DEPARTMENT TYPE
     * ============================================================
     */

    public static Specification<DepartmentEntity> hasDepartmentType(
        String departmentType
    ) {

        return (root, query, cb) -> {

            if (
                departmentType == null ||
                departmentType.isBlank()
            ) {
                return null;
            }

            return cb.equal(
                cb.lower(
                    root.get("departmentType")
                ),
                departmentType.trim().toLowerCase()
            );
        };
    }

    /*
     * ============================================================
     * ACTIVE
     * ============================================================
     */

    public static Specification<DepartmentEntity> hasActive(
        Boolean active
    ) {

        return (root, query, cb) -> {

            if (active == null) {
                return null;
            }

            return cb.equal(
                root.get("active"),
                active
            );
        };
    }

    /*
     * ============================================================
     * DELETED
     * ============================================================
     */

    public static Specification<DepartmentEntity> hasDeleted(
        Boolean deleted
    ) {

        return (root, query, cb) -> {

            if (deleted == null) {
                return null;
            }

            return cb.equal(
                root.get("deleted"),
                deleted
            );
        };
    }

    /*
     * ============================================================
     * CREATED FROM
     * ============================================================
     */

    public static Specification<DepartmentEntity> createdFrom(
        LocalDateTime date
    ) {

        return (root, query, cb) -> {

            if (date == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(
                root.get("createdAt"),
                date
            );
        };
    }

    /*
     * ============================================================
     * CREATED TO
     * ============================================================
     */

    public static Specification<DepartmentEntity> createdTo(
        LocalDateTime date
    ) {

        return (root, query, cb) -> {

            if (date == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(
                root.get("createdAt"),
                date
            );
        };
    }

    /*
     * ============================================================
     * UPDATED FROM
     * ============================================================
     */

    public static Specification<DepartmentEntity> updatedFrom(
        LocalDateTime date
    ) {

        return (root, query, cb) -> {

            if (date == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(
                root.get("updatedAt"),
                date
            );
        };
    }

    /*
     * ============================================================
     * UPDATED TO
     * ============================================================
     */

    public static Specification<DepartmentEntity> updatedTo(
        LocalDateTime date
    ) {

        return (root, query, cb) -> {

            if (date == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(
                root.get("updatedAt"),
                date
            );
        };
    }
}