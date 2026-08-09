package com.esign.platform.organizationmanagement.employee;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.esign.platform.common.exception.BusinessException;
import com.esign.platform.common.exception.ResourceNotFoundException;
import com.esign.platform.organizationmanagement.employee.dto.req.CreateEmployeeRequestDTO;
import com.esign.platform.organizationmanagement.employee.dto.req.UpdateEmployeeRequestDTO;
import com.esign.platform.organizationmanagement.employee.dto.res.EmployeeResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO request) {

        if (employeeRepository.existsByEmployeeCodeAndDeletedFalse(request.getEmployeeCode())) {
            throw new BusinessException("Employee Code already exists.");
        }

        if (employeeRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new BusinessException("Email already exists.");
        }

        Employee employee = new Employee();

        employee.setOrganizationId(request.getOrganizationId());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDesignation(request.getDesignation());

        employee = employeeRepository.save(employee);

        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(UUID employeeId,
                                              UpdateEmployeeRequestDTO request) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        employee.setOrganizationId(request.getOrganizationId());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDesignation(request.getDesignation());

        if (request.getActive() != null) {
            employee.setActive(request.getActive());
        }

        employee = employeeRepository.save(employee);

        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteEmployee(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        employee.setDeleted(true);
        employee.setActive(false);

        employeeRepository.save(employee);

    }

    /**
     * Convert Entity -> DTO
     */
    private EmployeeResponseDTO mapToResponse(Employee employee) {

        EmployeeResponseDTO response = new EmployeeResponseDTO();

        response.setId(employee.getId());
        response.setOrganizationId(employee.getOrganizationId());
        response.setDepartmentId(employee.getDepartmentId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhoneNumber(employee.getPhoneNumber());
        response.setDesignation(employee.getDesignation());
        response.setActive(employee.getActive());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());

        return response;
    }

}