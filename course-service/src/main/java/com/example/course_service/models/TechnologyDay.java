package com.example.course_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "technology_days",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"technology_id","day_number"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
public class TechnologyDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_id", nullable = false)
    private Technology technology;

    @Column(nullable = false)
    private Integer dayNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer estimatedHours;
}
