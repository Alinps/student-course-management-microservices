package com.example.batch_service.dto;

import com.example.batch_service.enums.BatchSchedule;
import com.example.batch_service.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchPatchRequest {

    private Long courseId;
    private String batchCode;
    private String batchName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxSeats;
    private BatchSchedule schedule;
    private BatchStatus status;

}
