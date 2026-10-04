package com.example.course_service.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseTechnologyRequest {


    @NotNull(message = "Technology Id is required")
    private Long technologyId;

    @NotNull(message = "Technology display order is required")
    private Integer displayOrder;

    @NotNull(message = "Duration is required")
    private Integer durationDays;

    private Boolean optional;

}
