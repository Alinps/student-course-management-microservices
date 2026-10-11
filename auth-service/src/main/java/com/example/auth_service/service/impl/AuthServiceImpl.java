package com.example.auth_service.service.impl;

import com.example.auth_service.client.EmployeeServiceClient;
import com.example.auth_service.client.StudentServiceClient;
import com.example.auth_service.config.JwtAuthenticationFilter;
import com.example.auth_service.dto.internal.CreateEmployeeInternalRequest;
import com.example.auth_service.dto.internal.CreateStudentInternalRequest;
import com.example.auth_service.dto.request.EmployeeRegistrationRequest;
import com.example.auth_service.dto.request.LoginRequest;

import com.example.auth_service.dto.request.StudentRegistrationRequest;
import com.example.auth_service.dto.response.LoginResponse;

import com.example.auth_service.enums.RoleName;
import com.example.auth_service.exception.InvalidCredentialException;
import com.example.auth_service.exception.ResourceAlreadyExistsException;
import com.example.auth_service.exception.ResourceNotFoundException;
import com.example.auth_service.exception.ResourceProcessingException;
import com.example.auth_service.models.Role;
import com.example.auth_service.models.User;
import com.example.auth_service.repository.RoleRepository;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.AuthService;
import com.example.auth_service.service.JwtService;
import com.example.auth_service.service.RefreshTokenService;
import com.example.auth_service.dto.request.RefreshTokenRequest;
import com.example.auth_service.service.RotatedRefreshToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final StudentServiceClient studentServiceClient;
    private final EmployeeServiceClient employeeServiceClient;
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);



    private User registerUser( String username,
                               String email,
                               String password,
                               RoleName roleName) {

        log.info("User registration requested");

        if (userRepository.existsByUsername(username)) {

            log.warn("Registration failed. username={}, already exists", username);

            throw new ResourceAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {

            log.warn("Registration failed. email={}, already exists", email);

            throw new ResourceAlreadyExistsException("Email already exists");
        }


        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found "+ roleName));

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .roles(new HashSet<>(Set.of(role)))
                .build();

        return userRepository.save(user);
    }

    private void deleteUser(User user) {

        user.getRoles().clear();

        userRepository.save(user);
        log.info("deleted partially created user {}", user.getUsername());

        userRepository.delete(user);
    }

    @Override
    public void registerStudent(StudentRegistrationRequest request) {

        log.info("Student registration requested");

        User user = registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                RoleName.STUDENT
        );

        try {
            CreateStudentInternalRequest studentRequest =
                    new CreateStudentInternalRequest(
                            user.getId(),
                            request.getName(),
                            user.getEmail()
                    );

            studentServiceClient.createStudent(studentRequest);

        } catch (Exception exception) {
            log.error("Student registration failed", exception);
            deleteUser(user);
            throw new ResourceProcessingException("Student registration failed", exception);
        }


    }

    @Override
    public void registerEmployee(EmployeeRegistrationRequest request) {

        log.info("Employee registration requested");

       User user = registerUser(
               request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                RoleName.EMPLOYEE);

       try {

           CreateEmployeeInternalRequest employeeRequest =
                   new CreateEmployeeInternalRequest(
                           user.getId(),
                           request.getFirstName(),
                           request.getLastName(),
                           user.getEmail(),
                           request.getEmployeeCode(),
                           request.getEmployeeDepartment(),
                           request.getDesignation()
                   );
           employeeServiceClient.createEmployee(employeeRequest);

       } catch (Exception exception) {

           log.info("Employee registration failed", exception);

           deleteUser(user);

           throw new ResourceProcessingException("Employee registration failed",exception);
       }


    }


    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialException(
                                "Invalid username or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialException(
                    "Invalid username or password"
            );
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new InvalidCredentialException(
                    "User account is disabled"
            );
        }

        // Generate the access JWT.
        String accessToken = jwtService.generateToken(user);

        // Generate and persist the refresh token.
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(
                        jwtService.getExpirationMillis() / 1000
                )
                .build();
    }


    /**
     * Refreshes the client's authentication tokens.
     *
     * The refresh-token service validates and rotates the submitted token.
     * After successful rotation, this method generates a new access JWT
     * for the user associated with the refresh token.
     */
    @Override
    public LoginResponse refresh(RefreshTokenRequest request) {

        // Validate the existing refresh token and rotate it.
        RotatedRefreshToken rotatedToken =
                refreshTokenService.rotateRefreshToken(
                        request.getRefreshToken()
                );

        // Retrieve the user associated with the validated refresh token.
        User user = rotatedToken.user();

        // Generate a new access JWT using the existing JWT service.
        String accessToken = jwtService.generateToken(user);

        // Return the new access token and replacement refresh token.
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(rotatedToken.rawToken())
                .tokenType("Bearer")
                .expiresIn(
                        jwtService.getExpirationMillis() / 1000
                )
                .build();
    }


    /**
     * Logs out the client by revoking its refresh token.
     *
     * The refresh-token service handles the database lookup and revocation.
     */
    @Override
    public void logout(RefreshTokenRequest request) {

        refreshTokenService.revokeRefreshToken(
                request.getRefreshToken()
        );

        log.info("Logout request processed");
    }

}
