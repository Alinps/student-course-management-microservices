package com.example.course_service.mapper;


import com.example.course_service.dto.TechnologyDayPatchRequest;
import com.example.course_service.dto.TechnologyDayRequest;
import com.example.course_service.dto.TechnologyDayResponse;
import com.example.course_service.dto.TechnologyResponse;
import com.example.course_service.models.Technology;
import com.example.course_service.models.TechnologyDay;
import org.mapstruct.*;

import java.util.List;
/*
MapStruct, a code generation library that automatically creates implementations for
mapping between DTOs and entities. Instead of writing conversion code manually,
MapStruct generates it at compile time.
*/
@Mapper(componentModel = "spring") /*Tells MapStruct that this interface is a
                                    mapper and that it should generate an implementation.

                                    componentModel = "spring"
                                    the generated implementation becomes a Spring Bean.*/
public interface TechnologyDayMapper {

    @Mapping(target="id", ignore = true) //Configures how one field should be mapped. Here it will ignore id field
    @Mapping(target = "technology", ignore = true)
    TechnologyDay toEntity(TechnologyDayRequest request);

    @Mapping(source = "technology.id", target = "technologyId")
    TechnologyDayResponse toResponseDTO(TechnologyDay technologyDay);
    List<TechnologyDayResponse> toResponseDTOList(List<TechnologyDay> technologyDayList);

    @Mapping(target = "id" ,ignore = true)
    @Mapping(target = "technology", ignore = true)
    void updateEntity(
            TechnologyDayRequest request,
            @MappingTarget TechnologyDay technologyDay
    );


    @BeanMapping( //Configures mapping behavior for the entire method, not just one field. Apply these rules while mapping this object.

            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE //This enum tells MapStruct:Ignore every property whose value is null.
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "technology", ignore = true)
    void patchEntity(
            TechnologyDayPatchRequest request,
            //Tells MapStruct "Do not create a new object. Update the existing object."
            @MappingTarget TechnologyDay technologyDay
    );

}
