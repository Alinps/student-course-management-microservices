package com.example.student_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class StudentUpdateRequest {

    @NotBlank(message="Name is required")
    private String name;

    @Email(message="Invalid email")
    private String email;
    
    @Pattern(
        regexp="^[0-9]{10}$",
        message="Phone number must contain exactly 10 digit"
    )
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
}
