package com.example.student_batch_assignment_service.repository;

import com.example.student_batch_assignment_service.models.BatchStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchStudentRepository extends JpaRepository<BatchStudent, Long> {
    List<BatchStudent> findByBatchId(Long batchId);
    List<BatchStudent> findByStudentId(Long studentId);
}
