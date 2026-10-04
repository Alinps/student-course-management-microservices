package com.example.student_batch_assignment_service.service;

import com.example.student_batch_assignment_service.dto.BatchStudentRequest;
import com.example.student_batch_assignment_service.dto.BatchStudentResponse;

import java.util.List;

public interface BatchStudentService {
    BatchStudentResponse createBatchStudent(BatchStudentRequest request);
    List<BatchStudentResponse> getAllBatchStudents();
    List<BatchStudentResponse> getBatchStudentByBatchId(Long batchId);
    List<BatchStudentResponse> getBatchStudentByStudentId(Long studentId);

}
