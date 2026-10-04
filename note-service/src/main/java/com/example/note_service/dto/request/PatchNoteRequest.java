package com.example.note_service.dto.request;

import com.example.note_service.enums.NoteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchNoteRequest {
    private String title;
    private String content;
    private NoteStatus status;
}
