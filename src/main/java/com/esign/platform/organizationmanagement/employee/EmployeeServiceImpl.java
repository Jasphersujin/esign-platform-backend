package com.esign.platform.organizationmanagement.employee;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.esign.platform.common.exception.BusinessException;
import com.esign.platform.common.exception.ResourceNotFoundException;
import com.esign.platform.organizationmanagement.department.entity.DepartmentEntity;
import com.esign.platform.organizationmanagement.department.repository.DepartmentRepository;
import com.esign.platform.organizationmanagement.employee.dto.req.CreateEmployeeRequestDTO;
import com.esign.platform.organizationmanagement.employee.dto.req.UpdateEmployeeRequestDTO;
import com.esign.platform.organizationmanagement.employee.dto.res.EmployeeResponseDTO;
import com.esign.platform.organizationmanagement.organization.entity.OrganizationEntity;
import com.esign.platform.organizationmanagement.organization.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;
    
    private final OrganizationRepository organizationRepository;


    @Override
    public EmployeeResponseDTO createEmployee(
            CreateEmployeeRequestDTO request) {

        if (employeeRepository
                .existsByEmployeeCodeAndDeletedFalse(
                        request.getEmployeeCode())) {

            throw new BusinessException(
                    "Employee Code already exists.");
        }

        if (employeeRepository
                .existsByEmailAndDeletedFalse(
                        request.getEmail())) {

            throw new BusinessException(
                    "Email already exists.");
        }

        Employee employee = new Employee();

        employee.setOrganizationId(
                request.getOrganizationId());

        employee.setDepartmentId(
                request.getDepartmentId());

        employee.setEmployeeCode(
                request.getEmployeeCode());

        employee.setFirstName(
                request.getFirstName());

        employee.setLastName(
                request.getLastName());

        employee.setEmail(
                request.getEmail());

        employee.setPhoneNumber(
                request.getPhoneNumber());

        employee.setDesignation(
                request.getDesignation());

        employee = employeeRepository.save(employee);

        return mapToResponse(employee);
    }


    @Override
    public EmployeeResponseDTO updateEmployee(
            UUID employeeId,
            UpdateEmployeeRequestDTO request) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found."));

        employee.setOrganizationId(
                request.getOrganizationId());

        employee.setDepartmentId(
                request.getDepartmentId());

        employee.setFirstName(
                request.getFirstName());

        employee.setLastName(
                request.getLastName());

        employee.setEmail(
                request.getEmail());

        employee.setPhoneNumber(
                request.getPhoneNumber());

        employee.setDesignation(
                request.getDesignation());

        if (request.getActive() != null) {
            employee.setActive(
                    request.getActive());
        }

        employee =
                employeeRepository.save(employee);

        return mapToResponse(employee);
    }


    @Override
    public EmployeeResponseDTO getEmployeeById(
            UUID employeeId) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found."));

        return mapToResponse(employee);
    }


    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {

        List<Employee> employees =
                employeeRepository.findAll();

        // ============================
        // ORGANIZATION IDs
        // ============================

        List<UUID> organizationIds =
                employees.stream()
                        .map(Employee::getOrganizationId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());

        // ============================
        // DEPARTMENT IDs
        // ============================

        List<UUID> departmentIds =
                employees.stream()
                        .map(Employee::getDepartmentId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());

        // ============================
        // ORGANIZATION NAMES
        // ============================

        Map<UUID, String> organizationNames =
                organizationRepository
                        .findAllByIdInAndDeletedFalse(
                                organizationIds
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                OrganizationEntity::getId,
                                OrganizationEntity::getOrgName
                        ));

        // ============================
        // DEPARTMENT NAMES
        // ============================

        Map<UUID, String> departmentNames =
                departmentRepository
                        .findAllByIdInAndDeletedFalse(
                                departmentIds
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                DepartmentEntity::getId,
                                DepartmentEntity::getDepartmentName
                        ));

        // ============================
        // RESPONSE
        // ============================

        return employees.stream()
                .map(employee ->
                        mapToResponse(
                                employee,
                                organizationNames.get(
                                        employee.getOrganizationId()
                                ),
                                departmentNames.get(
                                        employee.getDepartmentId()
                                )
                        )
                )
                .collect(Collectors.toList());
    }


    @Override
    public void deleteEmployee(UUID employeeId) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found."));

        employee.setDeleted(true);
        employee.setActive(false);

        employeeRepository.save(employee);
    }


    /**
     * Convert Entity -> DTO
     *
     * Used for create, update and get-by-id.
     */
//    private EmployeeResponseDTO mapToResponse(
//            Employee employee) {
//
//        String departmentName = null;
//
//        if (employee.getDepartmentId() != null) {
//
//            departmentName =
//                    departmentRepository
//                            .findByIdAndDeletedFalse(
//                                    employee.getDepartmentId())
//                            .map(DepartmentEntity::getDepartmentName)
//                            .orElse(null);
//        }
//
//        return mapToResponse(
//                employee,
//                departmentName);
//    }
    
    private EmployeeResponseDTO mapToResponse(
            Employee employee) {

        String organizationName = null;

        if (employee.getOrganizationId() != null) {
            organizationName =
                    organizationRepository
                            .findByIdAndDeletedFalse(
                                    employee.getOrganizationId())
                            .map(OrganizationEntity::getOrgName)
                            .orElse(null);
        }

        String departmentName = null;

        if (employee.getDepartmentId() != null) {
            departmentName =
                    departmentRepository
                            .findByIdAndDeletedFalse(
                                    employee.getDepartmentId())
                            .map(DepartmentEntity::getDepartmentName)
                            .orElse(null);
        }

        return mapToResponse(
                employee,
                organizationName,
                departmentName);
    }


    /**
     * Convert Entity -> DTO
     *
     * Used by getAllEmployees().
     */
//    private EmployeeResponseDTO mapToResponse(
//            Employee employee,
//            String departmentName) {
//
//        EmployeeResponseDTO response =
//                new EmployeeResponseDTO();
//
//        response.setId(
//                employee.getId());
//
//        response.setOrganizationId(
//                employee.getOrganizationId());
//
//        response.setDepartmentId(
//                employee.getDepartmentId());
//
//        response.setDepartmentName(
//                departmentName);
//
//        response.setEmployeeCode(
//                employee.getEmployeeCode());
//
//        response.setFirstName(
//                employee.getFirstName());
//
//        response.setLastName(
//                employee.getLastName());
//
//        response.setEmail(
//                employee.getEmail());
//
//        response.setPhoneNumber(
//                employee.getPhoneNumber());
//
//        response.setDesignation(
//                employee.getDesignation());
//
//        response.setActive(
//                employee.getActive());
//
//        response.setCreatedAt(
//                employee.getCreatedAt());
//
//        response.setUpdatedAt(
//                employee.getUpdatedAt());
//
//        return response;
//    }
    
    private EmployeeResponseDTO mapToResponse(
            Employee employee,
            String organizationName,
            String departmentName) {

        EmployeeResponseDTO response =
                new EmployeeResponseDTO();

        response.setId(
                employee.getId());

        response.setOrganizationId(
                employee.getOrganizationId());

        response.setOrganizationName(
                organizationName);

        response.setDepartmentId(
                employee.getDepartmentId());

        response.setDepartmentName(
                departmentName);

        response.setEmployeeCode(
                employee.getEmployeeCode());

        response.setFirstName(
                employee.getFirstName());

        response.setLastName(
                employee.getLastName());

        response.setEmail(
                employee.getEmail());

        response.setPhoneNumber(
                employee.getPhoneNumber());

        response.setDesignation(
                employee.getDesignation());

        response.setActive(
                employee.getActive());

        response.setCreatedAt(
                employee.getCreatedAt());

        response.setUpdatedAt(
                employee.getUpdatedAt());

        return response;
    }
}