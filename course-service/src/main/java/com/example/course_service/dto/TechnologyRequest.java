package com.example.course_service.dto;

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
public class TechnologyRequest {

    @NotBlank(message="Technology code is required")
    private String technologyCode;

    @NotBlank(message="Technology name is required")
    private String technologyName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Status field is required")
    private Boolean active;
}
