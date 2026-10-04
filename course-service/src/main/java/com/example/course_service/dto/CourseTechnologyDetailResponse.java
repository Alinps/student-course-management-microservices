package com.example.course_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseTechnologyDetailResponse {

    private Long technologyId;
    private String technologyName;
    private Integer displayOrder;
    private Integer duration;
    private boolean optional;

    private List<TechnologyDayDetailResponse> days;

}
