package com.example.employee_service.dto;

import com.example.employee_service.enums.EmployeeDepartment;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeInternalRequest {

    @NotNull
    private Long authUserId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String employeeCode;

    @NotNull
    private EmployeeDepartment employeeDepartment;

    @NotBlank
    private String designation;

}
