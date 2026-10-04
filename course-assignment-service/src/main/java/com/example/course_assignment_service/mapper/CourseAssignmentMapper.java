package com.example.course_assignment_service.mapper;

import com.example.course_assignment_service.dto.CourseAssignmentPatchRequest;
import com.example.course_assignment_service.dto.CourseAssignmentRequest;
import com.example.course_assignment_service.dto.CourseAssignmentResponse;
import com.example.course_assignment_service.models.CourseAssignment;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseAssignmentMapper {

    @Mapping(target = "id", ignore = true)
    CourseAssignment toEntity(CourseAssignmentRequest request);
    CourseAssignmentResponse toResponseDTO(CourseAssignment entity);

    List<CourseAssignmentResponse> toResponseDTOList(List<CourseAssignment> entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "batchId", ignore = true)
    @Mapping(target = "trainerId", ignore = true)
    @Mapping(target = "courseTechnologyId", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(
            CourseAssignmentRequest request,
            @MappingTarget CourseAssignment entity
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "batchId", ignore = true)
    @Mapping(target = "trainerId", ignore = true)
    @Mapping(target = "courseTechnologyId", ignore = true)
    @Mapping(target = "status", ignore = true)
    void patchEntity(
            CourseAssignmentPatchRequest request,
            @MappingTarget CourseAssignment entity
    );
}
