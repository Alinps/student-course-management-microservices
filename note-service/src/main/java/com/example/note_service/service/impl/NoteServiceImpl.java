package com.example.note_service.service.impl;

import com.example.note_service.client.CourseServiceClient;
import com.example.note_service.dto.request.CreateNoteRequest;
import com.example.note_service.dto.request.PatchNoteRequest;
import com.example.note_service.dto.request.UpdateNoteRequest;
import com.example.note_service.dto.response.NoteResponse;
import com.example.note_service.dto.response.TechnologyResponse;
import com.example.note_service.enums.NoteStatus;
import com.example.note_service.exception.ResourceNotFoundException;
import com.example.note_service.mapper.NoteMapper;
import com.example.note_service.models.Note;
import com.example.note_service.repository.NoteRepository;
import com.example.note_service.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private NoteRepository noteRepository;
    private CourseServiceClient courseServiceClient;
    private NoteMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    public NoteServiceImpl(
            NoteRepository noteRepository,
            CourseServiceClient courseServiceClient,
            NoteMapper mapper) {
        this.noteRepository = noteRepository;
        this.courseServiceClient = courseServiceClient;
        this.mapper = mapper;
    }

    @Override
    public NoteResponse createNote(CreateNoteRequest request) {

        log.info("Creating note for courseTechnologyId={}", request.getCourseTechnologyId());

        TechnologyResponse technologyResponse = courseServiceClient.getTechnology(request.getCourseTechnologyId());

        Note note = mapper.toEntity(request);
        note.setCourseTechnologyId(technologyResponse.getId());
        Note savedNote = noteRepository.save(note);

        log.info(
                "Note created successfully. noteId={}, courseTechnologyId={}",
                savedNote.getId(),
                savedNote.getCourseTechnologyId()
        );

        NoteResponse response = mapper.toResponseDTO(savedNote);

        response.setTechnologyCode(technologyResponse.getTechnologyCode());
        response.setTechnologyName(technologyResponse.getTechnologyName());
        response.setDescription(technologyResponse.getDescription());
        response.setActive(technologyResponse.getActive());

        return response;

    }

    @Override
    public NoteResponse getNoteById(Long id) {

        Note note = noteRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Note not found with id: " + id)
                );

        TechnologyResponse technologyResponse = courseServiceClient.getTechnology(note.getCourseTechnologyId());

        NoteResponse response = mapper.toResponseDTO(note);

        response.setTechnologyCode(technologyResponse.getTechnologyCode());
        response.setTechnologyName(technologyResponse.getTechnologyName());
        response.setDescription(technologyResponse.getDescription());
        response.setActive(technologyResponse.getActive());

        return response;

    }

    @Override
    public List<NoteResponse> getNotesByTechnologyId(Long technologyId) {

        log.debug("Fetching notes for courseTechnologyId={}", technologyId);


        List<Note> notes = noteRepository.findByCourseTechnologyId(technologyId);

        if (notes == null || notes.isEmpty()) {

            log.warn("No notes found for courseTechnologyId={}", technologyId);

            throw new ResourceNotFoundException("No notes found with id: " + technologyId);
        }

        log.debug(
                "Found {} notes for courseTechnologyId={}",
                notes.size(),
                technologyId
        );

        TechnologyResponse technologyResponse = courseServiceClient.getTechnology(technologyId);

        List<NoteResponse> responses = mapper.toResponseDTOList(notes);

        for (NoteResponse response : responses) {
            response.setTechnologyCode(technologyResponse.getTechnologyCode());
            response.setTechnologyName(technologyResponse.getTechnologyName());
            response.setDescription(technologyResponse.getDescription());
            response.setActive(technologyResponse.getActive());
        }

        return responses;

    }

    @Override
    public NoteResponse updateNote(Long id,UpdateNoteRequest request) {

        log.info("Updating note. noteId={}", id);

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Note not found while updating note. noteId={}", id);

                    return new ResourceNotFoundException("Note not found with id: " + id);
                });


        TechnologyResponse technologyResponse = courseServiceClient.getTechnology(note.getCourseTechnologyId());

        mapper.updateEntity(request,note);

        if (request.getStatus() != null) {
            note.setStatus(request.getStatus());
        }

        Note savedNote = noteRepository.save(note);

        NoteResponse response = mapper.toResponseDTO(savedNote);

        response.setTechnologyCode(technologyResponse.getTechnologyCode());
        response.setTechnologyName(technologyResponse.getTechnologyName());
        response.setDescription(technologyResponse.getDescription());
        response.setActive(technologyResponse.getActive());


        log.info(
                "Note updated successfully. noteId={}, courseTechnologyId={}",
                savedNote.getId(),
                savedNote.getCourseTechnologyId()
        );

        return response;
    }

    @Override
    public NoteResponse patchNote(Long id, PatchNoteRequest request) {

        log.info("Patching note. noteId={}", id);

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Note not found while patching note. noteId={}", id);

                    return new ResourceNotFoundException("Note not found with id: " + id);
                });

        TechnologyResponse technologyResponse = courseServiceClient.getTechnology(note.getCourseTechnologyId());

        mapper.patchEntity(request,note);

        if (request.getStatus() != null) {
            note.setStatus(request.getStatus());
        }

        Note savedNote = noteRepository.save(note);

        NoteResponse response = mapper.toResponseDTO(savedNote);

        response.setTechnologyCode(technologyResponse.getTechnologyCode());
        response.setTechnologyName(technologyResponse.getTechnologyName());
        response.setDescription(technologyResponse.getDescription());
        response.setActive(technologyResponse.getActive());

        log.info(
                "Note patched successfully. noteId={}, courseTechnologyId={}",
                savedNote.getId(),
                savedNote.getCourseTechnologyId()
        );


        return response;

    }

    @Override
    public void deleteNote(Long id) {

        log.info("Deleting note. noteId={}", id);

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Note not found for deletion. noteId={}", id);

                    return new ResourceNotFoundException("Note not found with id: " + id);
                });

        note.setStatus(NoteStatus.DELETED);
        noteRepository.save(note);


        log.info("Note deleted successfully. noteId={}", id);
    }
}
