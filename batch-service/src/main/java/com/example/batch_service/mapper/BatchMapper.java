package com.example.batch_service.mapper;

import com.example.batch_service.dto.BatchPatchRequest;
import com.example.batch_service.dto.BatchRequest;
import com.example.batch_service.dto.BatchResponse;
import com.example.batch_service.models.Batch;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring") // to generate the implementation of this interface as Spring Bean
public interface BatchMapper {

    @Mapping(target = "id", ignore = true)
    Batch toEntity(BatchRequest request); // to convert dto to entity
    BatchResponse toResponseDTO(Batch batch); // to convert entity to dto
    List<BatchResponse> toResponseDTOList(List<Batch> batches); // to convert list of entity to list of dto



    @Mapping(target = "id", ignore = true )
    @Mapping(target = "availableSeats", ignore = true)// to prevent accidental overwriting of fields managed by  database
    void updateEntity (
            BatchRequest request,
            @MappingTarget Batch batch /* @MappingTarget tells MapStruct not to create
                                        new Batch. update the existing one

                                        return type is void because of
                                       "I'm not creating a new entity; I'm updating the one you gave me." */
    );


    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    ) // ignore every field that is null
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableSeats", ignore = true)
    void patchEntity(
            BatchPatchRequest request,
            @MappingTarget Batch batch
    );

}
