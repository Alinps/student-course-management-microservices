package com.example.employee_service.controllers;


import com.example.employee_service.dto.*;
import com.example.employee_service.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/employee")
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
           @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response = employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        EmployeeResponse response = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/internal/{id:\\d+}")
    public ResponseEntity<EmployeeResponse> getInternalEmployeeById(
            @PathVariable Long id) {

        EmployeeResponse response = employeeService.getInternalEmployeeById(id);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployee() {

        List<EmployeeResponse> responses = employeeService.getAllEmployees();

        return ResponseEntity.ok(responses);

    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployeeAsAdmin(
           @PathVariable Long id,
           @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response = employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeProfileUpdateRequest request) {

        EmployeeResponse response = employeeService.updateEmployeeProfile(id, request);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> patchEmployee (
            @PathVariable  Long id,
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response = employeeService.patchEmployee(id, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/internal/by-ids")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployeeByIds(@RequestBody IdsRequest request) {
        List<EmployeeResponse> responses = employeeService.getEmployeeByIds(request.getIds());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/internal")
    public ResponseEntity<EmployeeResponse> createInternalEmployee(
            @Valid @RequestBody CreateEmployeeInternalRequest request) {
        EmployeeResponse response = employeeService.createInternalEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
