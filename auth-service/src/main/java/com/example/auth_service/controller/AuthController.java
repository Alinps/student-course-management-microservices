package com.example.auth_service.controller;

import com.example.auth_service.dto.request.EmployeeRegistrationRequest;
import com.example.auth_service.dto.request.LoginRequest;
import com.example.auth_service.dto.request.RegisterRequest;
import com.example.auth_service.dto.request.StudentRegistrationRequest;
import com.example.auth_service.dto.response.LoginResponse;
import com.example.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(
            @Valid @RequestBody StudentRegistrationRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok("Student registered successfully");
    }

    @PostMapping("/register/employee")
    public ResponseEntity<String> registerEmployee(
            @Valid @RequestBody EmployeeRegistrationRequest request) {
        authService.registerEmployee(request);
        return ResponseEntity.ok("Employee registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }


}
