package com.example.course_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "technologies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String technologyCode;

    @Column(nullable = false, unique = true)
    private String technologyName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean active;
    
    /*
    @OneToMany(mappedBy = "technology")  The CourseTechnology.technology field already defines the database relationship. Don't create another relationship from this side
    private List<CourseTechnology> courseTechnologies; one Technology has many CourseTechnology records.
    */

}
