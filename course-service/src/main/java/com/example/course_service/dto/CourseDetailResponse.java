package com.example.course_service.dto;

import com.example.course_service.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailResponse {

    private Long id;
    private String courseCode;
    private String courseName;
    private String description;
    private Integer duration;
    private BigDecimal fee;
    private String category;
    private CourseStatus status;

    private List<CourseTechnologyDetailResponse> technologies;
}
