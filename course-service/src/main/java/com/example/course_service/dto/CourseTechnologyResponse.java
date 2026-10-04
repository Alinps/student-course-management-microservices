package com.example.course_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseTechnologyResponse {

    private Long id;
    private Long courseId;
    private String courseName;
    private Long technologyId;
    private String technologyName;
    private Integer displayOrder;
    private Integer durationDays;
    private Boolean optional;

}
