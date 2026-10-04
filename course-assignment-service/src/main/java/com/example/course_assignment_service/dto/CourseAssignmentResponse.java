package com.example.course_assignment_service.dto;

import com.example.course_assignment_service.enums.AssignmentRole;
import com.example.course_assignment_service.enums.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentResponse {

    private Long id;
    private Long batchId;
    private String batchName;
    private Long trainerId;
    private String trainerName;
    private Long courseTechnologyId;
    private String courseTechnologyName;
//    private Integer fromDay;
//    private Integer toDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private AssignmentStatus status;

}
