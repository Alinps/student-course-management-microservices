package com.example.course_assignment_service.dto;


import com.example.course_assignment_service.enums.EmployeeDepartment;
import com.example.course_assignment_service.enums.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private EmployeeDepartment department;
    private EmployeeStatus status;
}
