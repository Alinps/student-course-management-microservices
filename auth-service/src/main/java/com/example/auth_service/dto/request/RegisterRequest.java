package com.example.auth_service.dto.request;

import com.example.auth_service.enums.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message="Username is required")
    @Size(min=3, max=50)
    private String username;

    @NotBlank(message="Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=8, max=100)
    private String password;

    @NotNull(message = "Role is required")
    private RoleName roleName;
}
