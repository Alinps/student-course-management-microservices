package com.example.student_batch_assignment_service.dto;

import com.example.student_batch_assignment_service.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchStudentRequest {
    @NotNull
    private Long batchId;

    @NotNull
    private Long studentId;

    @NotNull
    private Date enrollmentDate;

    private EnrollmentStatus enrollmentStatus;

}
