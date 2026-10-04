package com.example.course_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyPatchRequest {
    private Long id;
    private String technologyCode;
    private String technologyName;
    private String description;
    private Boolean active;

}
