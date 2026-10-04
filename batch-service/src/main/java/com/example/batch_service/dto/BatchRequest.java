package com.example.batch_service.dto;

import com.example.batch_service.enums.BatchSchedule;
import com.example.batch_service.enums.BatchStatus;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchRequest {

//    @NotNull(message = "Course ID is required")
//    private Long courseId;

    @NotBlank(message="Batch code is required")
    private String batchCode;

    @NotBlank(message = "Batch name is required")
    private String batchName;

    @NotNull(message="Start Date is required")
    private LocalDate startDate;

    @NotNull(message="End Date is required")
    private LocalDate endDate;

    @NotNull(message="Max Seat is required")
    @Min(1)
    private Integer maxSeats;

    @NotNull(message = "Batch schedule is required")
    private BatchSchedule schedule;

    private BatchStatus status;

}
