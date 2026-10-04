package com.example.course_service.mapper;


import com.example.course_service.dto.TechnologyPatchRequest;
import com.example.course_service.dto.TechnologyRequest;
import com.example.course_service.dto.TechnologyResponse;
import com.example.course_service.models.Technology;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TechnologyMapper {

    @Mapping(target = "id", ignore = true)
    Technology toEntity(TechnologyRequest request);
    TechnologyResponse toResponseDTO(Technology technology);
    List<TechnologyResponse> toResponseDTOList(List<Technology> technologyList);

    @Mapping(target = "id", ignore = true)
    void updateEntity(
            TechnologyRequest request,
            @MappingTarget Technology technology
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    void patchEntity(
            TechnologyPatchRequest request,
            @MappingTarget  Technology technology
    );


}
