package com.esign.platform.organizationmanagement.employee;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.esign.platform.common.dto.ApiResponse;
import com.esign.platform.organizationmanagement.employee.dto.CreateEmployeeRequestDTO;
import com.esign.platform.organizationmanagement.employee.dto.EmployeeResponseDTO;
import com.esign.platform.organizationmanagement.employee.dto.UpdateEmployeeRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Create Employee
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequestDTO request) {

        EmployeeResponseDTO response = employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<EmployeeResponseDTO>builder()
                        .success(true)
                        .message("Employee created successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Update Employee
     */
    @PutMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> updateEmployee(
            @PathVariable UUID employeeId,
            @Valid @RequestBody UpdateEmployeeRequestDTO request) {

        EmployeeResponseDTO response =
                employeeService.updateEmployee(employeeId, request);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponseDTO>builder()
                        .success(true)
                        .message("Employee updated successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Get Employee By Id
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getEmployeeById(
            @PathVariable UUID employeeId) {

        EmployeeResponseDTO response =
                employeeService.getEmployeeById(employeeId);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponseDTO>builder()
                        .success(true)
                        .message("Employee fetched successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Get All Employees
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getAllEmployees() {

        List<EmployeeResponseDTO> response =
                employeeService.getAllEmployees();

        return ResponseEntity.ok(
                ApiResponse.<List<EmployeeResponseDTO>>builder()
                        .success(true)
                        .message("Employees fetched successfully.")
                        .data(response)
                        .build());
    }

    /**
     * Delete Employee
     */
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<Object>> deleteEmployee(
            @PathVariable UUID employeeId) {

        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Employee deleted successfully.")
                        .build());
    }

}