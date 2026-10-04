package com.example.batch_service.controllers;

import com.example.batch_service.dto.BatchPatchRequest;
import com.example.batch_service.dto.BatchRequest;
import com.example.batch_service.dto.BatchResponse;
import com.example.batch_service.dto.IdsRequest;
import com.example.batch_service.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/batch")
@RestController
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    public ResponseEntity<BatchResponse> createBatch(
            @Valid @RequestBody  BatchRequest request) {

        BatchResponse response = batchService.createBatch(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchResponse> getBatchById(@PathVariable Long id) {

        BatchResponse response = batchService.getBatchById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<BatchResponse> getInternalBatchById(@PathVariable Long id) {

        BatchResponse response = batchService.getBatchById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BatchResponse>> getAllBatch() {

        List<BatchResponse> responses = batchService.getAllBatch();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBatch(
            @PathVariable Long id,
            @Valid @RequestBody  BatchRequest request) {

        BatchResponse response = batchService.updateBatch(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity patchBatch(
            @PathVariable Long id,
            @Valid @RequestBody BatchPatchRequest request) {

        BatchResponse response = batchService.patchBatch(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable  Long id) {

        batchService.deleteBatch(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/internal/by-ids")
    public ResponseEntity<List<BatchResponse>> getBatchesByIds(@RequestBody IdsRequest request) {
        List<BatchResponse> responses = batchService.getBatchesByIds(request.getIds());
        return ResponseEntity.ok(responses);
    }
}
