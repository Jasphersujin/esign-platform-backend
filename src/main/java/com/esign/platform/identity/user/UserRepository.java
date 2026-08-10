package com.esign.platform.identity.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find User by Id
     */
    Optional<User> findByIdAndDeletedFalse(UUID id);

    /**
     * Find User by Employee
     */
    Optional<User> findByEmployee_IdAndDeletedFalse(UUID employeeId);

    /**
     * Find User by Id with Role.
     *
     * Used by JWT authentication because the
     * authentication filter needs roleCode.
     */
    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.role
        WHERE u.id = :userId
        AND u.deleted = false
    """)
    Optional<User> findByIdAndDeletedFalseWithRole(
            @Param("userId") UUID userId
    );
    /**
     * Find User by Email
     */
    Optional<User> findByEmailAndDeletedFalse(String email);

    /**
     * Check Email Exists
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Check Employee Already Has User
     */
    boolean existsByEmployee_Id(UUID employeeId);

    /**
     * Active Users
     */
    List<User> findByDeletedFalse();

    /**
     * Active Users by Role
     */
    List<User> findByRole_IdAndDeletedFalse(UUID roleId);

    /**
     * Active Users by Organization
     */
    List<User> findByEmployee_OrganizationIdAndDeletedFalse(UUID organizationId);

    /**
     * Active Users by Department
     */
    List<User> findByEmployee_DepartmentIdAndDeletedFalse(UUID departmentId);

}