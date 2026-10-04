package com.example.course_service.service;

import com.example.course_service.dto.CourseTechnologyPatchRequest;
import com.example.course_service.dto.CourseTechnologyRequest;
import com.example.course_service.dto.CourseTechnologyResponse;
import com.example.course_service.models.CourseTechnology;

import java.util.List;

public interface CourseTechnologyService {

    CourseTechnologyResponse createCourseTechnology(
            Long courseId,
            CourseTechnologyRequest request);

    List<CourseTechnologyResponse> getTechnologiesByCourse(Long courseId);

    CourseTechnologyResponse getCourseTechnologyById(Long id);

    CourseTechnologyResponse updateCourseTechnology(Long id, CourseTechnologyRequest request);

    CourseTechnologyResponse patchCourseTechnology(Long id, CourseTechnologyPatchRequest request);

    void deleteCourseTechnology(Long id);
    List<CourseTechnologyResponse> getCourseTechnologiesById(List<Long> ids);
}
