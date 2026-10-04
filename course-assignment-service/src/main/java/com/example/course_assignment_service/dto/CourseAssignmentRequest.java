package com.example.course_assignment_service.dto;

import com.example.course_assignment_service.enums.AssignmentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentRequest {

    @NotNull
    private Long batchId;

    @NotNull
    private Long courseTechnologyId;

    @NotNull
    private Long trainerId;

//    @NotNull
//    @Min(1)
//    private Integer fromDay;
//
//    @NotNull
//    @Min(1)
//    private Integer toDay;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private AssignmentStatus status;


}
