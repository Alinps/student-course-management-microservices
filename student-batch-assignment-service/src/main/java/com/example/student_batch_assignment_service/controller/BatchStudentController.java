package com.example.student_batch_assignment_service.controller;

import com.example.student_batch_assignment_service.dto.BatchStudentRequest;
import com.example.student_batch_assignment_service.dto.BatchStudentResponse;
import com.example.student_batch_assignment_service.service.BatchStudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/student-batch-assignment")
@RestController
public class BatchStudentController {

    private final BatchStudentService batchStudentService;

    public BatchStudentController(BatchStudentService batchStudentService) {
        this.batchStudentService = batchStudentService;
    }

    @PostMapping
    public ResponseEntity<BatchStudentResponse> createBatchStudent(
            @Valid @RequestBody BatchStudentRequest request) {
        return ResponseEntity.ok(batchStudentService.createBatchStudent(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BatchStudentResponse>> getAllBatchStudents() {
        return ResponseEntity.ok(batchStudentService.getAllBatchStudents());
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<BatchStudentResponse>> getBatchStudentByBatchId(@PathVariable Long batchId) {
        List<BatchStudentResponse> batchStudent = batchStudentService.getBatchStudentByBatchId(batchId);
        return  ResponseEntity.ok(batchStudent);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<BatchStudentResponse>> getBatchStudentByStudentId(@PathVariable Long studentId) {
        List<BatchStudentResponse> batchStudent = batchStudentService.getBatchStudentByStudentId(studentId);
        return  ResponseEntity.ok(batchStudent);
    }
}
