package com.example.employee_service.dto;

import com.example.employee_service.enums.EmployeeDepartment;
import com.example.employee_service.enums.EmployeeStatus;


import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class EmployeeRequest {

    @NotBlank(message="Employee code is required.")
    private String employeeCode;

    @NotBlank(message="First name is required.")
    private String firstName;

    @NotBlank(message="Last name is required.")
    private String lastName;

    @NotBlank(message="Email is required.")
    @Email(message="Invalid email.")
    private String email;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message="Phone number must contain exactly 10 digit."
    )
    private String phone;

    @NotNull
    private EmployeeDepartment department;

    @NotBlank(message = "Designation is required.")
    private String designation;

    @NotBlank(message="Qualification is required.")
    private String qualification;

    @NotNull
    @Min(0)
    private Integer experience;

    private LocalDate hireDate;

    @NotNull
    private EmployeeStatus status;




    public String getEmployeeCode() {
        return employeeCode;
    }
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EmployeeDepartment getDepartment() {
        return department;
    }
    public void setDepartment(EmployeeDepartment department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getQualification() {
        return qualification;
    }
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getExperience() {
        return experience;
    }
    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public EmployeeStatus getStatus() {
        return status;
    }
    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }
}
