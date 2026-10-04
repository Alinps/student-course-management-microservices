package com.example.auth_service.dto.request;

import com.example.auth_service.enums.EmployeeDepartment;
import com.example.auth_service.enums.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRegistrationRequest {

    @NotBlank(message="Username is required")
    @Size(min=3, max=50)
    private String username;

    @NotBlank(message="Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=8, max=100)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String employeeCode;

    @NotNull
    private EmployeeDepartment employeeDepartment;

    @NotBlank
    private String designation;

}
