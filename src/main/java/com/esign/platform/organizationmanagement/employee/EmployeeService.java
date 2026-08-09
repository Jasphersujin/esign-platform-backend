package com.esign.platform.organizationmanagement.employee;

import java.util.List;
import java.util.UUID;

import com.esign.platform.organizationmanagement.employee.dto.req.CreateEmployeeRequestDTO;
import com.esign.platform.organizationmanagement.employee.dto.req.UpdateEmployeeRequestDTO;
import com.esign.platform.organizationmanagement.employee.dto.res.EmployeeResponseDTO;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO request);

    EmployeeResponseDTO updateEmployee(UUID employeeId,
                                       UpdateEmployeeRequestDTO request);

    EmployeeResponseDTO getEmployeeById(UUID employeeId);

    List<EmployeeResponseDTO> getAllEmployees();

    void deleteEmployee(UUID employeeId);

}