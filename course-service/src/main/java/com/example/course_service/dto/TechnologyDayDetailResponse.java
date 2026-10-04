package com.example.course_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDayDetailResponse {

    private Long id;
    private Integer dayNumber;
    private String title;
    private String description;
    private Integer estimatedHours;

}
