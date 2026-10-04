package com.example.note_service.mapper;

import com.example.note_service.dto.request.CreateNoteRequest;
import com.example.note_service.dto.request.PatchNoteRequest;
import com.example.note_service.dto.request.UpdateNoteRequest;
import com.example.note_service.dto.response.NoteResponse;
import com.example.note_service.models.Note;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mapping(target="id", ignore = true)
    @Mapping(target="courseTechnologyId", ignore = true)
    @Mapping(target="status", ignore=true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Note toEntity(CreateNoteRequest request);

    @Mapping(target="technologyCode", ignore = true)
    @Mapping(target="technologyName", ignore = true)
    @Mapping(target="description", ignore = true)
    @Mapping(target="active", ignore = true)
    NoteResponse toResponseDTO(Note entity);
    List<NoteResponse> toResponseDTOList(List<Note> entity);

    @Mapping(target="id", ignore = true)
    @Mapping(target="courseTechnologyId", ignore = true)
    @Mapping(target="status", ignore=true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            UpdateNoteRequest request,
            @MappingTarget Note entity
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="courseTechnologyId", ignore = true)
    @Mapping(target="status", ignore=true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void patchEntity(
            PatchNoteRequest request,
            @MappingTarget Note entity
    );
}
