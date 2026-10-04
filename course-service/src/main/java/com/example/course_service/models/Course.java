package com.example.course_service.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

import com.example.course_service.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String courseCode;

    @Column(nullable = false, unique = true)
    private String courseName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private BigDecimal fee;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus status;
/*
    @OneToMany(mappedBy = "course") The CourseTechnology.course field already defines the database relationship. Don't create another relationship from this side
    private List<CourseTechnology> courseTechnologies; There are many CourseTechnology records:
*/
}
