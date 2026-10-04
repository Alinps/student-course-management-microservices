package com.example.course_assignment_service.repository;

import com.example.course_assignment_service.models.CourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Long> {
    List<CourseAssignment> findByBatchId(Long batchId);
    List<CourseAssignment> findByCourseTechnologyId(Long courseTechnologyId);
    List<CourseAssignment>findByTrainerId(Long trainerId);
    boolean existsByTrainerId(Long trainerId);
    boolean existsByBatchId(Long batchId);
    boolean existsByCourseTechnologyId(Long courseTechnologyId);

    // @Query("""
    // SELECT COUNT(c) > 0
    // FROM CourseAssignment c
    // WHERE  c.batchId = :batchId
    //     AND c.courseTechnologyId = :courseTechnologyId
    //     AND c.fromDay <= :toDay
    //     AND c.toDay >= :fromDay
    // """)
    //     boolean existsOverlappingAssignment(
    //             Long batchId,
    //             Long courseTechnologyId,
    //             Integer fromDay,
    //             Integer toDay);
    //
}
