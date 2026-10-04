package com.example.note_service.dto.response;

import com.example.note_service.enums.NoteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    private Long id;
    private Long courseTechnologyId;
    private String technologyCode;
    private String technologyName;
    private String description;
    private Boolean active;
    private String title;
    private String content;
    private NoteStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
