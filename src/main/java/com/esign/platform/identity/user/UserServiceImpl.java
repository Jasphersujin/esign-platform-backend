package com.esign.platform.identity.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.esign.platform.accesscontrol.role.Role;
import com.esign.platform.accesscontrol.role.RoleRepository;
import com.esign.platform.common.exception.BusinessException;
import com.esign.platform.common.exception.ResourceNotFoundException;
import com.esign.platform.identity.auth.config.JwtService;
import com.esign.platform.identity.user.dto.CreateUserDTO;
import com.esign.platform.identity.user.dto.LoginDTO;
import com.esign.platform.identity.user.dto.LoginResponseDTO;
import com.esign.platform.identity.user.dto.UpdateUserDTO;
import com.esign.platform.identity.user.dto.UserResponseDTO;
import com.esign.platform.organizationmanagement.employee.Employee;
import com.esign.platform.organizationmanagement.employee.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponseDTO createUser(CreateUserDTO request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        Role role = roleRepository.findByIdAndDeletedFalse(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        if (userRepository.existsByEmployee_Id(request.getEmployeeId())) {
            throw new BusinessException("User already exists for this employee.");
        }

        if (userRepository.existsByEmailIgnoreCase(employee.getEmail())) {
            throw new BusinessException("Email already exists.");
        }

        User user = new User();

        user.setEmployee(employee);
        user.setRole(role);
        user.setEmail(employee.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerified(false);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setPasswordResetRequired(false);
        user.setPasswordChangedAt(LocalDateTime.now());

        user = userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public UserResponseDTO updateUser(UUID userId, UpdateUserDTO request) {

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        Role role = roleRepository.findByIdAndDeletedFalse(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        user.setRole(role);

        if (request.getAccountLocked() != null) {
            user.setAccountLocked(request.getAccountLocked());
        }

        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        user = userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public UserResponseDTO getUserById(UUID userId) {

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        return mapToResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<UserResponseDTO> getUsersByOrganization(UUID organizationId) {

        return userRepository
                .findByEmployee_OrganizationIdAndDeletedFalse(organizationId)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<UserResponseDTO> getUsersByDepartment(UUID departmentId) {

        return userRepository
                .findByEmployee_DepartmentIdAndDeletedFalse(departmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<UserResponseDTO> getUsersByRole(UUID roleId) {

        return userRepository
                .findByRole_IdAndDeletedFalse(roleId)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public void deleteUser(UUID userId) {

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        user.setDeleted(true);
        user.setActive(false);

        userRepository.save(user);
    }

    @Override
    public void lockUser(UUID userId) {

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        user.setAccountLocked(true);

        userRepository.save(user);

    }

    @Override
    public void unlockUser(UUID userId) {

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);

        userRepository.save(user);

    }

    @Override
    public void resetPassword(UUID userId, String newPassword) {

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordResetRequired(true);
        user.setPasswordChangedAt(LocalDateTime.now());

        userRepository.save(user);

    }

    @Override
    public void changePassword(UUID userId,
                               String oldPassword,
                               String newPassword) {

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BusinessException("Old password is incorrect.");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setPasswordResetRequired(false);

        userRepository.save(user);

    }

//    @Override
//    public UserResponseDTO login(LoginDTO request) {
//
//        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
//                .orElseThrow(() ->
//                        new BusinessException("Invalid Email or Password."));
//
//        if (user.getAccountLocked()) {
//            throw new BusinessException("Account is locked.");
//        }
//
//        if (!passwordEncoder.matches(
//                request.getPassword(),
//                user.getPasswordHash())) {
//
//            user.setFailedLoginAttempts(
//                    user.getFailedLoginAttempts() + 1);
//
//            userRepository.save(user);
//
//            throw new BusinessException("Invalid Email or Password.");
//        }
//
//        user.setFailedLoginAttempts(0);
//        user.setLastLoginAt(LocalDateTime.now());
//
//        userRepository.save(user);
//
//        return mapToResponse(user);
//    }
    
    @Override
    public LoginResponseDTO login(LoginDTO request) {

        /*
         * 1. Find user
         */
        User user =
                userRepository
                        .findByEmailAndDeletedFalse(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Invalid Email or Password."
                                )
                        );

        /*
         * 2. Check account locked
         */
        if (Boolean.TRUE.equals(
                user.getAccountLocked())) {

            throw new BusinessException(
                    "Account is locked."
            );
        }

        /*
         * 3. Check active
         */
        if (!Boolean.TRUE.equals(
                user.getActive())) {

            throw new BusinessException(
                    "User account is inactive."
            );
        }

        /*
         * 4. Verify password
         */
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            user.setFailedLoginAttempts(
                    user.getFailedLoginAttempts() + 1
            );

            userRepository.save(user);

            throw new BusinessException(
                    "Invalid Email or Password."
            );
        }

        /*
         * 5. Successful login
         */
        user.setFailedLoginAttempts(0);

        user.setLastLoginAt(
                LocalDateTime.now()
        );

        userRepository.save(user);

        /*
         * 6. Generate JWT
         */
        String accessToken =
                jwtService.generateToken(user);

        /*
         * 7. Return JWT + user information
         */
        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(mapToResponse(user))
                .build();
    }

    private UserResponseDTO mapToResponse(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .employeeId(user.getEmployee().getId())
                .employeeCode(user.getEmployee().getEmployeeCode())
                .employeeName(
                        user.getEmployee().getFirstName()
                        + " "
                        + user.getEmployee().getLastName())
                .roleId(user.getRole().getId())
                .roleCode(user.getRole().getRoleCode())
                .roleName(user.getRole().getRoleName())
                .email(user.getEmail())
                .emailVerified(user.getEmailVerified())
                .accountLocked(user.getAccountLocked())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .active(user.getActive())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }
}