package com.example.course_assignment_service.dto;

import com.example.course_assignment_service.enums.AssignmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentPatchRequest {

    @NotNull
    private Long batchId;

    @NotNull
    private Long courseTechnologyId;

    @NotNull
    private Long trainerId;

//    private Integer fromDay;
//    private Integer toDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private AssignmentStatus status;

}
