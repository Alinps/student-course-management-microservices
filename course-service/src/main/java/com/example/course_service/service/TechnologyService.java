package com.example.course_service.service;

import com.example.course_service.dto.TechnologyPatchRequest;
import com.example.course_service.dto.TechnologyRequest;
import com.example.course_service.dto.TechnologyResponse;
import com.example.course_service.models.Technology;

import java.util.List;

public interface TechnologyService {

    TechnologyResponse createTechnology(TechnologyRequest request);
    TechnologyResponse getTechnologyById(Long id);
    List<TechnologyResponse> getAllTechnologies();
    TechnologyResponse updateTechnology(Long id, TechnologyRequest request);
    TechnologyResponse patchTechnology(Long id, TechnologyPatchRequest request);
    void deleteTechnology(Long id);
    public TechnologyResponse getInternalTechnologyById(Long id);

}
