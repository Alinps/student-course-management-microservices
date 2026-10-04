package com.example.student_batch_assignment_service.dto;

import com.example.student_batch_assignment_service.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchStudentPatchRequest {

    private Long batchId;

    private Long studentId;

    private Date enrollmentDate;

    private EnrollmentStatus enrollmentStatus;
}
