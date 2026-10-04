package com.example.batch_service.models;

import jakarta.persistence.*;

import java.time.LocalDate;

import com.example.batch_service.enums.*;

import lombok.*;


@Entity
@Table(name= "batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
 Reference to course service
    @Column(nullable = false)
 private Long courseId;
*/

    @Column(nullable = false, unique = true, length = 50)
    private String batchCode;

    @Column(nullable = false)
    private String batchName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer maxSeats;

    private Integer availableSeats;

    @Column(nullable = false)
    private BatchSchedule schedule;

    @Column(nullable = false)
    private BatchStatus status;


}
