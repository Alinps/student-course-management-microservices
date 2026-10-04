package com.example.course_service.service;

import com.example.course_service.dto.TechnologyDayPatchRequest;
import com.example.course_service.dto.TechnologyDayRequest;
import com.example.course_service.dto.TechnologyDayResponse;

import java.util.List;


public interface TechnologyDayService {

    TechnologyDayResponse createDaySplit(Long technologyId, TechnologyDayRequest request);
    List<TechnologyDayResponse> getDaySplitByTechnologyId(Long technologyId);
    List<TechnologyDayResponse> getAllDaySplit();
    TechnologyDayResponse getDaySplitById(Long id);
    TechnologyDayResponse updateDaySplit(Long id, Long technologyId ,TechnologyDayRequest request);
    TechnologyDayResponse patchDaySplit(Long id, Long technologyId ,TechnologyDayPatchRequest request);
    void deleteDaySplit(Long id);



}
