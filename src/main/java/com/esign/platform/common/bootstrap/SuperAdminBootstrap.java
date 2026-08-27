package com.esign.platform.common.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esign.platform.accesscontrol.role.Role;
import com.esign.platform.accesscontrol.role.RoleRepository;
import com.esign.platform.accesscontrol.role.RoleType;
import com.esign.platform.identity.user.User;
import com.esign.platform.identity.user.UserRepository;
import com.esign.platform.organizationmanagement.employee.Employee;
import com.esign.platform.organizationmanagement.employee.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuperAdminBootstrap implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.super-admin.email}")
    private String superAdminEmail;

    @Value("${app.bootstrap.super-admin.password}")
    private String superAdminPassword;

    @Value("${app.bootstrap.super-admin.employee-code}")
    private String superAdminEmployeeCode;

    @Override
    @Transactional
    public void run(String... args) {

        System.out.println("==========================================");
        System.out.println("Starting Super Admin Bootstrap...");
        System.out.println("==========================================");

        /*
         * 1. Create / Get Super Admin Role
         */
        Role superAdminRole = createSuperAdminRole();

        /*
         * 2. Create / Get Super Admin Employee
         */
        Employee superAdminEmployee =
                createSuperAdminEmployee();

        /*
         * 3. Create / Get Super Admin User
         */
        createSuperAdminUser(
                superAdminRole,
                superAdminEmployee
        );

        System.out.println("==========================================");
        System.out.println("Super Admin Bootstrap Completed.");
        System.out.println("==========================================");
    }

    /**
     * Create / Get Super Admin Role
     *
     * roleCode  -> stable system identifier
     * roleName  -> human-readable display name
     */
    private Role createSuperAdminRole() {

        return roleRepository
                .findByRoleCodeAndDeletedFalse("SUPER_ADMIN")
                .orElseGet(() -> {

                    Role role = new Role();

                    /*
                     * Super Admin is a platform-level role.
                     * It does not belong to any organization.
                     */
                    role.setOrganization(null);

                    /*
                     * Stable system identifier
                     */
                    role.setRoleCode("SUPER_ADMIN");

                    /*
                     * Human-readable name
                     */
                    role.setRoleName("Super Admin");

                    role.setDescription(
                            "System administrator with full platform access."
                    );

                    /*
                     * Global/System level role
                     */
                    role.setRoleType(RoleType.GLOBAL);

                    /*
                     * Prevent normal users from modifying/deleting
                     * this role.
                     */
                    role.setSystemRole(true);

                    role.setActive(true);
                    role.setDeleted(false);

                    Role savedRole =
                            roleRepository.save(role);

                    System.out.println(
                            "Super Admin role created: "
                                    + savedRole.getId()
                    );

                    return savedRole;
                });
    }

    /**
     * Create / Get Super Admin Employee
     */
    private Employee createSuperAdminEmployee() {

        return employeeRepository
                .findByEmployeeCodeAndDeletedFalse(
                        superAdminEmployeeCode
                )
                .orElseGet(() -> {

                    Employee employee = new Employee();

                    /*
                     * Super Admin is a platform-level employee.
                     * Therefore organization_id is NULL.
                     */
                    employee.setOrganizationId(null);
                    employee.setDepartmentId(null);

                    employee.setEmployeeCode(
                            superAdminEmployeeCode
                    );

                    employee.setFirstName("Super");
                    employee.setLastName("Admin");

                    employee.setEmail(
                            superAdminEmail
                    );

                    employee.setPhoneNumber(null);

                    employee.setDesignation(
                            "Platform Administrator"
                    );

                    employee.setActive(true);
                    employee.setDeleted(false);

                    Employee savedEmployee =
                            employeeRepository.save(employee);

                    System.out.println(
                            "Super Admin employee created: "
                                    + savedEmployee.getId()
                    );

                    return savedEmployee;
                });
    }

    /**
     * Create Super Admin User
     */
    private void createSuperAdminUser(
            Role superAdminRole,
            Employee superAdminEmployee) {

        /*
         * Check whether this employee already has a user.
         */
        if (userRepository.existsByEmployee_Id(
                superAdminEmployee.getId())) {

            System.out.println(
                    "Super Admin user already exists."
            );

            return;
        }

        User user = new User();

        /*
         * Employee mapping
         */
        user.setEmployee(superAdminEmployee);

        /*
         * Role mapping
         *
         * users.role_id
         *       ↓
         * roles.id
         */
        user.setRole(superAdminRole);

        /*
         * Login email
         */
        user.setEmail(
                superAdminEmail
        );

        /*
         * Password is stored as BCrypt hash.
         */
        user.setPasswordHash(
                passwordEncoder.encode(
                        superAdminPassword
                )
        );

        user.setEmailVerified(true);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setPasswordResetRequired(false);

        user.setActive(true);
        user.setDeleted(false);

        User savedUser =
                userRepository.save(user);

        System.out.println(
                "Super Admin user created: "
                        + savedUser.getId()
        );
    }
}