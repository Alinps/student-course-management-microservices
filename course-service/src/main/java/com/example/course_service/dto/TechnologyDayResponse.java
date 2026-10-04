package com.example.course_service.dto;

import com.example.course_service.models.Technology;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDayResponse {

    private Long id;
    private Long technologyId;
    private Integer dayNumber;
    private String title;
    private String description;
    private Integer estimatedHours;

}
