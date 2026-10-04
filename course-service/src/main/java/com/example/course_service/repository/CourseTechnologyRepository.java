package com.example.course_service.repository;

import com.example.course_service.models.Course;
import com.example.course_service.models.CourseTechnology;
import com.example.course_service.models.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.course_service.dto.CourseDetailRow;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseTechnologyRepository extends JpaRepository<CourseTechnology, Long> {

    List<CourseTechnology> findByCourse(Course course);
    Optional<CourseTechnology> findByTechnology(Technology technology);
    List<CourseTechnology> findByCourseId(Long courseId);
    boolean existsByCourseAndTechnology(Course course, Technology technology);

    @Query(
            """
SELECT new  com.example.course_service.dto.CourseDetailRow(
c.id,
c.courseCode,
c.courseName,
c.description,
c.duration,
c.fee,
c.category,
c.status,

t.id,
t.technologyName,
ct.displayOrder,
ct.durationDays,
ct.optional,

td.id,
td.dayNumber,
td.title,
td.description,
td.estimatedHours

) 
FROM CourseTechnology ct
JOIN ct.course c
JOIN ct.technology t
LEFT JOIN TechnologyDay td 
    ON td.technology.id = t.id
WHERE c.id = :courseId
ORDER BY ct.displayOrder ASC, td.dayNumber ASC
"""
    )
    List<CourseDetailRow> findCourseDetail(@Param("courseId") Long courseId);

}
