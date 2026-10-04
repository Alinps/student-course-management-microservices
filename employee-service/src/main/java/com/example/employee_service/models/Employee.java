package com.example.employee_service.models;

import jakarta.persistence.*;


import java.time.LocalDate;
import com.example.employee_service.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_user_id", nullable = false, unique = true)
    private Long authUserId;

    @Column(nullable = false, unique = true)
    private String employeeCode;

    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private EmployeeDepartment department;

    private String designation;

    private String qualification;

    private Integer experience;

    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status =  EmployeeStatus.ACTIVE;

}
