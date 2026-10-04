package com.example.note_service.dto.request;

import com.example.note_service.enums.NoteStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteRequest {
    @NotBlank(message="Title is required")
    private String title;

    @NotBlank(message="Content is required")
    private String content;

    private NoteStatus status;
}
