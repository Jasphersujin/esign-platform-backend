package com.esign.platform.identity.auth.security;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.esign.platform.identity.user.User;

@Component
public class CurrentUserContext {

    private static final String SUPER_ADMIN_ROLE_CODE = "SUPER_ADMIN";

    /**
     * Get the currently authenticated user.
     *
     * JwtAuthenticationFilter puts the User entity
     * into SecurityContext as the authenticated principal.
     */
    public User getUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new IllegalStateException(
                    "No authenticated user."
            );
        }

        Object principal =
                authentication.getPrincipal();

        if (!(principal instanceof User user)) {

            throw new IllegalStateException(
                    "Authenticated principal is not a User."
            );
        }

        return user;
    }

    // =========================================================
    // USER INFORMATION
    // =========================================================

    /**
     * Current logged-in user's ID.
     */
    public UUID getUserId() {

        return getUser().getId();
    }

    /**
     * Current logged-in user's employee ID.
     */
    public UUID getEmployeeId() {

        return getUser()
                .getEmployee()
                .getId();
    }

    /**
     * Current logged-in user's email.
     */
    public String getEmail() {

        return getUser().getEmail();
    }

    // =========================================================
    // ORGANIZATION / DEPARTMENT
    // =========================================================

    /**
     * Get the organization ID of the current user.
     *
     * GLOBAL:
     *     returns null
     *
     * ORGANIZATION:
     *     returns organization ID
     *
     * DEPARTMENT:
     *     returns organization ID
     */
    public UUID getOrganizationId() {

        return getUser()
                .getEmployee()
                .getOrganizationId();
    }

    /**
     * Get the department ID of the current user.
     *
     * GLOBAL:
     *     returns null
     *
     * ORGANIZATION:
     *     returns null
     *
     * DEPARTMENT:
     *     returns department ID
     */
    public UUID getDepartmentId() {

        return getUser()
                .getEmployee()
                .getDepartmentId();
    }

    // =========================================================
    // ROLE
    // =========================================================

    /**
     * Current logged-in user's role ID.
     */
    public UUID getRoleId() {

        return getUser()
                .getRole()
                .getId();
    }

    /**
     * Current logged-in user's role code.
     *
     * Example:
     *
     * SUPER_ADMIN
     * ORGANIZATION_ADMIN
     * DEPARTMENT_USER
     */
    public String getRoleCode() {

        return getUser()
                .getRole()
                .getRoleCode();
    }

    /**
     * Current logged-in user's role name.
     */
    public String getRoleName() {

        return getUser()
                .getRole()
                .getRoleName();
    }

    // =========================================================
    // SCOPE
    // =========================================================

    /**
     * Determine the current user's data-access scope.
     *
     * Rules:
     *
     * SUPER_ADMIN
     *     organizationId = null
     *     departmentId   = null
     *     scope           = GLOBAL
     *
     * ORGANIZATION
     *     organizationId = present
     *     departmentId   = null
     *     scope           = ORGANIZATION
     *
     * DEPARTMENT
     *     organizationId = present
     *     departmentId   = present
     *     scope           = DEPARTMENT
     */
    public ScopeLevel getScopeLevel() {

        User user = getUser();

        String roleCode =
                user.getRole().getRoleCode();

        UUID organizationId =
                user.getEmployee().getOrganizationId();

        UUID departmentId =
                user.getEmployee().getDepartmentId();

        // -----------------------------------------------------
        // SUPER ADMIN
        // -----------------------------------------------------

        if (SUPER_ADMIN_ROLE_CODE.equals(roleCode)) {

            /*
             * Super Admin must not belong to an organization
             * or department.
             */
            if (organizationId != null
                    || departmentId != null) {

                throw new IllegalStateException(
                        "Invalid Super Admin scope. "
                        + "Super Admin cannot have organization "
                        + "or department scope."
                );
            }

            return ScopeLevel.GLOBAL;
        }

        // -----------------------------------------------------
        // DEPARTMENT LEVEL
        // -----------------------------------------------------

        if (organizationId != null
                && departmentId != null) {

            return ScopeLevel.DEPARTMENT;
        }

        // -----------------------------------------------------
        // ORGANIZATION LEVEL
        // -----------------------------------------------------

        if (organizationId != null
                && departmentId == null) {

            return ScopeLevel.ORGANIZATION;
        }

        // -----------------------------------------------------
        // INVALID SCOPE
        // -----------------------------------------------------

        throw new IllegalStateException(
                "Unable to determine user scope. "
                + "User must have a valid organization "
                + "or department scope."
        );
    }

    // =========================================================
    // SCOPE CHECKS
    // =========================================================

    /**
     * Is current user Super Admin / Global scope?
     */
    public boolean isGlobalScope() {

        return getScopeLevel() == ScopeLevel.GLOBAL;
    }

    /**
     * Is current user Organization scope?
     */
    public boolean isOrganizationScope() {

        return getScopeLevel() == ScopeLevel.ORGANIZATION;
    }

    /**
     * Is current user Department scope?
     */
    public boolean isDepartmentScope() {

        return getScopeLevel() == ScopeLevel.DEPARTMENT;
    }

    /**
     * Is current user Super Admin?
     */
    public boolean isSuperAdmin() {

        return SUPER_ADMIN_ROLE_CODE.equals(
                getRoleCode()
        );
    }

    // =========================================================
    // SCOPE VALIDATION HELPERS
    // =========================================================

    /**
     * Check whether the current user can access
     * the specified organization.
     *
     * GLOBAL:
     *     Can access every organization.
     *
     * ORGANIZATION:
     *     Can access only its own organization.
     *
     * DEPARTMENT:
     *     Can access only its own organization.
     */
    public boolean canAccessOrganization(
            UUID organizationId) {

        if (organizationId == null) {
            return false;
        }

        /*
         * Super Admin can access everything.
         */
        if (isGlobalScope()) {
            return true;
        }

        /*
         * Organization and Department users
         * can only access their own organization.
         */
        return organizationId.equals(
                getOrganizationId()
        );
    }

    /**
     * Check whether the current user can access
     * the specified department.
     *
     * GLOBAL:
     *     Can access every department.
     *
     * ORGANIZATION:
     *     Can access every department inside
     *     their organization.
     *
     * DEPARTMENT:
     *     Can access only their own department.
     */
    public boolean canAccessDepartment(
            UUID organizationId,
            UUID departmentId) {

        if (organizationId == null
                || departmentId == null) {

            return false;
        }

        /*
         * Super Admin can access everything.
         */
        if (isGlobalScope()) {
            return true;
        }

        /*
         * First validate organization.
         */
        if (!organizationId.equals(
                getOrganizationId())) {

            return false;
        }

        /*
         * Organization-level user can access
         * departments inside their organization.
         */
        if (isOrganizationScope()) {
            return true;
        }

        /*
         * Department-level user can access
         * only their own department.
         */
        return departmentId.equals(
                getDepartmentId()
        );
    }

    // =========================================================
    // SCOPE ENUM
    // =========================================================

    public enum ScopeLevel {

        GLOBAL,

        ORGANIZATION,

        DEPARTMENT
    }
}