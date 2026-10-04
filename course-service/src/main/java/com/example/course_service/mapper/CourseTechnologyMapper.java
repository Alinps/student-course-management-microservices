package com.example.course_service.mapper;


import com.example.course_service.dto.CourseTechnologyPatchRequest;
import com.example.course_service.dto.CourseTechnologyRequest;
import com.example.course_service.dto.CourseTechnologyResponse;
import com.example.course_service.models.CourseTechnology;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CourseTechnologyMapper {

    @Mapping(target="id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "technology", ignore = true)
    CourseTechnology toEntity(CourseTechnologyRequest request);
    /*
    MapStruct automatically handles:
    id           → id
    displayOrder → displayOrder
    durationDays → durationDays
    optional     → optional

    But it doesn't automatically infer:

    course.id      → courseId
    technology.id  → technologyId

    So you explicitly tell it:

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "technology.id", target = "technologyId")
    */
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.courseName", target = "courseName")
    @Mapping(source = "technology.id", target = "technologyId")
    @Mapping(source = "technology.technologyName", target = "technologyName")
    CourseTechnologyResponse toResponseDTO(CourseTechnology entity);




    List<CourseTechnologyResponse> toResponseDTOList(List<CourseTechnology> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "technology", ignore = true)
    void updateEntity(
            CourseTechnologyRequest request,
            @MappingTarget CourseTechnology entity);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "technology", ignore = true)
    void pathEntity(
            CourseTechnologyPatchRequest request,
            @MappingTarget CourseTechnology entity);
}
