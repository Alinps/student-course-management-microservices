package com.example.employee_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfileUpdateRequest {
    @NotBlank(message="First name is required.")
    private String firstName;

    @NotBlank(message="Last name is required.")
    private String lastName;

    @NotBlank(message="Email is required.")
    private String email;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message="Phone number must contain exactly 10 digit."
    )
    private String phone;

    @NotBlank(message="Qualification is required.")
    private String qualification;

    @NotBlank(message="Experience is required.")
    private Integer experience;
}
