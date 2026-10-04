package com.example.student_batch_assignment_service.dto;

import com.example.student_batch_assignment_service.enums.BatchStatus;

import com.example.student_batch_assignment_service.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchStudentResponse {

    private Long id;
    private Long batchId;
    private String batchName;
    private String batchCode;
    private EnrollmentStatus status;
    private BatchStatus batchStatus;
    private Long studentId;
    private String studentName;
    private String email;
    private String phone;
}
