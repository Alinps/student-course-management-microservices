package com.example.course_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "course_technologies",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "course_id",
                                "technology_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false) //Many CourseTechnology records can belong to one Course.
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_id", nullable = false) //Many CourseTechnology records can refer to one Technology.
    private Technology technology;

    @Column(nullable = false)
    private Integer displayOrder;

    @Column(nullable = false)
    private Integer durationDays;

    @Column(nullable = false)
    private Boolean optional;

}
