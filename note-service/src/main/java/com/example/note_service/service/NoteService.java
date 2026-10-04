package com.example.note_service.service;

import com.example.note_service.dto.request.CreateNoteRequest;
import com.example.note_service.dto.request.PatchNoteRequest;
import com.example.note_service.dto.request.UpdateNoteRequest;
import com.example.note_service.dto.response.NoteResponse;

import java.util.List;

public interface NoteService {
    public NoteResponse createNote(CreateNoteRequest request);
    public NoteResponse getNoteById(Long id);
    public List<NoteResponse> getNotesByTechnologyId(Long technologyId);
    public NoteResponse updateNote(Long id, UpdateNoteRequest request);
    public NoteResponse patchNote(Long id, PatchNoteRequest request);
    void deleteNote(Long id);
}
