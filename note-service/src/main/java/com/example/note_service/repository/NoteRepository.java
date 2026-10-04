package com.example.note_service.repository;

import com.example.note_service.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByCourseTechnologyId(Long courseTechnologyId);
}
