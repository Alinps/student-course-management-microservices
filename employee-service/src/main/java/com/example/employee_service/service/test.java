//package com.example.employee_service.service;
//
//
//
//import com.example.employee_service.dto.EmployeeRequest;
//import com.example.employee_service.dto.EmployeeResponse;
//import com.example.employee_service.enums.EmployeeStatus;
//import com.example.employee_service.exception.*;
//import com.example.employee_service.models.Employee;
//import com.example.employee_service.repository.EmployeeRepository;
//
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Service
//public class EmployeeServiceImpl implements EmployeeService{
//
//    private final EmployeeRepository employeeRepository;
//
//    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//
//
//    @Override
//    public EmployeeResponse createEmployee(EmployeeRequest request) {
//
//
//
//        if (employeeRepository.existsByEmail(request.getEmail())) {
//            throw new ResourceAlreadyExistsException("Email already exists.");
//        }
//
//        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
//            throw new ResourceAlreadyExistsException("Employee code already exists.");
//        }
//
//        if (employeeRepository.existsByPhone(request.getPhone())) {
//            throw new ResourceAlreadyExistsException("Phone number already exists.");
//        }
//
//        Employee employee = new Employee();
//
//        employee.setEmployeeCode(request.getEmployeeCode());
//        employee.setFirstName(request.getFirstName());
//        employee.setLastName(request.getLastName());
//        employee.setEmail(request.getEmail());
//        employee.setPhone(request.getPhone());
//        employee.setDepartment(request.getDepartment());
//        employee.setDesignation(request.getDesignation());
//        employee.setQualification(request.getQualification());
//        employee.setExperience(request.getExperience());
//        employee.setHireDate(LocalDate.now());
//        employee.setStatus(request.getStatus());
//
//        Employee savedEmployee = employeeRepository.save(employee);
//
//        EmployeeResponse response = new EmployeeResponse();
//
//        response.setId(savedEmployee.getId());
//        response.setEmployeeCode(savedEmployee.getEmployeeCode());
//        response.setFirstName(savedEmployee.getFirstName());
//        response.setLastName(savedEmployee.getLastName());
//        response.setEmail(savedEmployee.getEmail());
//        response.setPhone(savedEmployee.getPhone());
//        response.setDepartment(savedEmployee.getDepartment());
//        response.setDesignation(savedEmployee.getDesignation());
//        response.setQualification(savedEmployee.getQualification());
//        response.setExperience(savedEmployee.getExperience());
//        response.setHireDate(savedEmployee.getHireDate());
//        response.setStatus(savedEmployee.getStatus());
//
//        return response;
//
//    }
//
//
//    @Override
//    public EmployeeResponse getEmployeeById(Long id) {
//
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> {
//                    return new ResourceNotFoundException("Employee not found with id: " + id);
//                });
//
//        EmployeeResponse response = new EmployeeResponse();
//
//        response.setId(employee.getId());
//        response.setEmployeeCode(employee.getEmployeeCode());
//        response.setFirstName(employee.getFirstName());
//        response.setLastName(employee.getLastName());
//        response.setEmail(employee.getEmail());
//        response.setPhone(employee.getPhone());
//        response.setDepartment(employee.getDepartment());
//        response.setDesignation(employee.getDesignation());
//        response.setQualification(employee.getQualification());
//        response.setExperience(employee.getExperience());
//        response.setHireDate(employee.getHireDate());
//        response.setStatus(employee.getStatus());
//
//        return response;
//
//
//    }
//
//    @Override
//    public List<EmployeeResponse> getAllEmployees() {
//
//        List<Employee> employees = employeeRepository.findAll();
//
//        List<EmployeeResponse> responses = new ArrayList<>();
//
//        for (Employee employee: employees) {
//
//            EmployeeResponse response = new EmployeeResponse();
//
//            response.setId(employee.getId());
//            response.setEmployeeCode(employee.getEmployeeCode());
//            response.setFirstName(employee.getFirstName());
//            response.setLastName(employee.getLastName());
//            response.setEmail(employee.getEmail());
//            response.setPhone(employee.getPhone());
//            response.setDesignation(employee.getDesignation());
//            response.setDepartment(employee.getDepartment());
//            response.setExperience(employee.getExperience());
//            response.setQualification(employee.getQualification());
//            response.setHireDate(employee.getHireDate());
//            response.setStatus(employee.getStatus());
//
//            responses.add(response);
//
//        }
//
//        return  responses;
//
//    }
//
//
//    @Override
//    public void deleteEmployee(Long id) {
//
//        // check if employee exist
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> {
//                    return new ResourceNotFoundException("Employee not found with id: " + id);
//                });
//
//
//
//        // delete employee
//        employee.setStatus(EmployeeStatus.DELETED);
//        employeeRepository.save(employee);
//
//
//    }
//
//
//}
