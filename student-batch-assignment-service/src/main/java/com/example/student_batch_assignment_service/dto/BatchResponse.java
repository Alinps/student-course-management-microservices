package com.example.student_batch_assignment_service.dto;

import com.example.student_batch_assignment_service.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchResponse {
    private Long id;
    private String batchCode;
    private String batchName;
    private BatchStatus status;
}
