package com.example.batch_service.service;


import com.example.batch_service.dto.BatchPatchRequest;
import com.example.batch_service.dto.BatchRequest;
import com.example.batch_service.dto.BatchResponse;

import java.util.List;

public interface BatchService {

    BatchResponse createBatch(BatchRequest request);
    BatchResponse getBatchById(Long id);
    List<BatchResponse> getAllBatch();
    BatchResponse updateBatch(Long id, BatchRequest request);
    BatchResponse patchBatch(Long id, BatchPatchRequest request);
    void deleteBatch(Long id);
    List<BatchResponse> getBatchesByIds(List<Long> ids);
    public BatchResponse getInternalBatchById(Long id);


}
