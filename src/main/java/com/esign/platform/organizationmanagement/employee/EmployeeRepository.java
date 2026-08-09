package com.esign.platform.organizationmanagement.employee;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

	Optional<Employee> findByEmployeeCodeAndDeletedFalse(
	        String employeeCode);

	Optional<Employee> findByEmailAndDeletedFalse(
	        String email);

	boolean existsByEmployeeCodeAndDeletedFalse(
	        String employeeCode);

	boolean existsByEmailAndDeletedFalse(
	        String email);
}