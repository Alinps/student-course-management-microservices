package com.example.course_service.repository;

import com.example.course_service.enums.CourseStatus;
import com.example.course_service.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);
    Optional<Course> findByCourseName(String courseName);
    Optional<Course> findByStatus(CourseStatus status);
    boolean existsByCourseCode(String courseCode);
    boolean existsByCourseName(String courseName);

}
