package com.esign.platform.organizationmanagement.employee;

import java.util.List;
import java.util.UUID;

import com.esign.platform.organizationmanagement.employee.dto.CreateEmployeeRequestDTO;
import com.esign.platform.organizationmanagement.employee.dto.EmployeeResponseDTO;
import com.esign.platform.organizationmanagement.employee.dto.UpdateEmployeeRequestDTO;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO request);

    EmployeeResponseDTO updateEmployee(UUID employeeId,
                                       UpdateEmployeeRequestDTO request);

    EmployeeResponseDTO getEmployeeById(UUID employeeId);

    List<EmployeeResponseDTO> getAllEmployees();

    void deleteEmployee(UUID employeeId);

}