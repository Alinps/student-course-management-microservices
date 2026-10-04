package com.example.course_assignment_service.dto;

import com.example.course_assignment_service.enums.CourseStatus;
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
    private String technologyName;
    private CourseStatus status;
}
