package com.example.auth_service.service;

import com.example.auth_service.dto.request.EmployeeRegistrationRequest;
import com.example.auth_service.dto.request.LoginRequest;
import com.example.auth_service.dto.request.StudentRegistrationRequest;
import com.example.auth_service.dto.response.LoginResponse;
import com.example.auth_service.dto.request.RefreshTokenRequest;


public interface AuthService {
    public void registerStudent(StudentRegistrationRequest request);
    public void registerEmployee(EmployeeRegistrationRequest request);
    public LoginResponse login(LoginRequest request);
    /**
     * Validates a refresh token and issues a new token pair.
     *
     * @param request contains the refresh token submitted by the client
     * @return a new access token and refresh token
     */
    LoginResponse refresh(RefreshTokenRequest request);

    /**
     * Logs out a client by revoking its refresh token.
     *
     * @param request contains the refresh token to revoke
     */
    void logout(RefreshTokenRequest request);
}
