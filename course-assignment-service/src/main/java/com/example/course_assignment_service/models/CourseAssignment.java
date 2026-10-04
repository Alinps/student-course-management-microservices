package com.example.course_assignment_service.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.course_assignment_service.enums.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "course_assignments",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "batch_id",
                                "course_technology_id",
                        }
                )
        }
)
public class CourseAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to Course Service
    @Column(nullable = false)
    private Long courseTechnologyId;

    // Reference to Batch Service
    @Column(nullable = false)
    private Long batchId;

    // Reference to Employee Service
    @Column(nullable = false)
    private Long trainerId;

//    @Column(nullable = false)
//    private Integer fromDay;
//
//    @Column(nullable = false)
//    private Integer toDay;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
