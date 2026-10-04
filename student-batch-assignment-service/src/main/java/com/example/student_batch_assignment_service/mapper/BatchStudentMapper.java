package com.example.student_batch_assignment_service.mapper;

import com.example.student_batch_assignment_service.dto.BatchStudentPatchRequest;
import com.example.student_batch_assignment_service.dto.BatchStudentRequest;
import com.example.student_batch_assignment_service.dto.BatchStudentResponse;
import com.example.student_batch_assignment_service.models.BatchStudent;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BatchStudentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "batchId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    BatchStudent toEntity(BatchStudentRequest request);

    @Mapping(target = "batchName", ignore = true)
    @Mapping(target = "batchCode", ignore = true)
    @Mapping(target = "batchStatus", ignore = true)
    @Mapping(target = "studentName", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "phone", ignore = true)
    BatchStudentResponse toResponseDTO(BatchStudent entity);
    List<BatchStudentResponse> toResponseDTOList(List<BatchStudent> entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "batchId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    void updateEntity(
            BatchStudentRequest request,
            @MappingTarget BatchStudent entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "batchId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    void patchEntity(
            BatchStudentPatchRequest request,
            @MappingTarget BatchStudent entity);
}
