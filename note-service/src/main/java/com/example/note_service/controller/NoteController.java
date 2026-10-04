package com.example.note_service.controller;

import com.example.note_service.dto.request.CreateNoteRequest;
import com.example.note_service.dto.request.PatchNoteRequest;
import com.example.note_service.dto.request.UpdateNoteRequest;
import com.example.note_service.dto.response.NoteResponse;
import com.example.note_service.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/notes")
@RestController
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(
            @Valid @RequestBody CreateNoteRequest request) {

        NoteResponse response = noteService.createNote(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable Long id) {
        NoteResponse response = noteService.getNoteById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/technology/{technologyId}")
    public ResponseEntity<List<NoteResponse>> getNoteByTechnologyId(@PathVariable Long technologyId) {
        List<NoteResponse> responses =  noteService.getNotesByTechnologyId(technologyId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNoteRequest request) {
        NoteResponse response = noteService.updateNote(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NoteResponse> patchNote(
            @PathVariable Long id,
            @Valid @RequestBody PatchNoteRequest request) {
        NoteResponse response = noteService.patchNote(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
