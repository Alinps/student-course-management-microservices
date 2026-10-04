package com.example.auth_service.service;

import com.example.auth_service.dto.request.EmployeeRegistrationRequest;
import com.example.auth_service.dto.request.LoginRequest;
import com.example.auth_service.dto.request.RegisterRequest;
import com.example.auth_service.dto.request.StudentRegistrationRequest;
import com.example.auth_service.dto.response.LoginResponse;
import com.example.auth_service.dto.response.RegisterResponse;

public interface AuthService {
    public void registerStudent(StudentRegistrationRequest request);
    public void registerEmployee(EmployeeRegistrationRequest request);
    public LoginResponse login(LoginRequest request);
}
