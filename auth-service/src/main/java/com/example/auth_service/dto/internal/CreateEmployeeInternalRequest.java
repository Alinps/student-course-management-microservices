package com.example.auth_service.dto.internal;

import com.example.auth_service.enums.EmployeeDepartment;
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


    private Long authUserId;

    private String firstName;

    private String lastName;

    private String email;

    private String employeeCode;

    private EmployeeDepartment employeeDepartment;

    private String designation;

}
