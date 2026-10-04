package com.example.employee_service.service;

import com.example.employee_service.dto.CreateEmployeeInternalRequest;
import com.example.employee_service.dto.EmployeeProfileUpdateRequest;
import com.example.employee_service.dto.EmployeeRequest;
import com.example.employee_service.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(Long id);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    EmployeeResponse patchEmployee(Long id, EmployeeRequest request);
    List<EmployeeResponse> getEmployeeByIds(List<Long> ids);
    void deleteEmployee(Long id);
    EmployeeResponse createInternalEmployee(CreateEmployeeInternalRequest request);
    EmployeeResponse updateEmployeeProfile(Long id, EmployeeProfileUpdateRequest request);
    EmployeeResponse getInternalEmployeeById(Long id);


}
