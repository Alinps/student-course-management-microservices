package com.example.batch_service.dto;

import com.example.batch_service.enums.BatchSchedule;
import com.example.batch_service.enums.BatchStatus;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchResponse {

    private Long id;
//    private Long courseId;
    private String batchCode;
    private String batchName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxSeats;
    private Integer availableSeats;
    private BatchSchedule schedule;
    private BatchStatus status;

}
