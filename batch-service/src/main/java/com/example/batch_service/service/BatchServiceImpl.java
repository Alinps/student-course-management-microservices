package com.example.batch_service.service;

import com.example.batch_service.dto.BatchPatchRequest;
import com.example.batch_service.dto.BatchRequest;
import com.example.batch_service.dto.BatchResponse;
import com.example.batch_service.exception.*;
import com.example.batch_service.mapper.BatchMapper;
import com.example.batch_service.models.Batch;
import com.example.batch_service.repository.BatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final BatchMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(BatchServiceImpl.class);

    public BatchServiceImpl(
            BatchRepository batchRepository,
            BatchMapper mapper) {
        this.batchRepository = batchRepository;
        this.mapper = mapper;
    }

    @Override
    public BatchResponse createBatch(BatchRequest request) {

        log.info(
                "Creating new batch. batchName={}, batchCode={}",
                request.getBatchName(),
                request.getBatchCode()
        );

        if (batchRepository.existsByBatchCode(request.getBatchCode())) {

            log.warn("Batch creation rejected. Batch code already exists. batchCode={}", request.getBatchCode());

            throw new BatchCodeAlreadyExistException("Batch code already exists");

        }

        if (batchRepository.existsByBatchName(request.getBatchName())) {

            log.warn("Batch creation rejected. Batch name already exists. batchName={}", request.getBatchName());

            throw new BatchNameAlreadyExistException("Batch name already exists.");
        }

        Batch batch = mapper.toEntity(request);
        batch.setAvailableSeats(request.getMaxSeats());
        Batch savedBatch = batchRepository.save(batch);

        log.info(
                "Batch created successfully. batchId={}, batchCode={}, batchName={}",
                savedBatch.getId(),
                savedBatch.getBatchCode(),
                savedBatch.getBatchName()
        );

        BatchResponse response = mapper.toResponseDTO(savedBatch);

        return response;

    }

    @Override
    public BatchResponse getBatchById(Long id) {

        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Failed to get batch. Batch not found. batchId={}",id);
                    return new BatchNotFoundException("Batch not found with id: " + id);

                });


        BatchResponse response = mapper.toResponseDTO(batch);

        return response;
    }


    @Override
    public BatchResponse getInternalBatchById(Long id) {

        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Failed to get batch for internal service. Batch not found. batchId={}",id);
                    return new BatchNotFoundException("Batch not found with id: " + id);

                });


        BatchResponse response = mapper.toResponseDTO(batch);

        return response;
    }

    @Override
    public List<BatchResponse> getAllBatch() {

        List<Batch> batches = batchRepository.findAll();

        List<BatchResponse> responses = mapper.toResponseDTOList(batches);

        return responses;

    }


    @Override
    public BatchResponse updateBatch(Long id, BatchRequest request) {

        log.info(
                "Updating Batch. batchId={}, batchCode={}, batchName={}",
                id,
                request.getBatchCode(),
                request.getBatchName()
        );

        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Failed to update batch. Batch not found. batchId={}",id);
                    return new BatchNotFoundException("Batch not found with id " + id );

                });


        batchRepository.findByBatchCode(request.getBatchCode())
                .ifPresent(existingBatch -> {
                    if (!existingBatch.getId().equals(id)) {

                        log.warn("Failed to update batch. Batch code already exists. batchId={}",id);
                        throw new BatchCodeAlreadyExistException("Batch code already exists");
                    }
                });

        batchRepository.findByBatchName(request.getBatchName())
                .ifPresent(existingBatch -> {
                    if (!existingBatch.getId().equals(id)) {

                        log.warn("Failed to update batch. Batch name already exists. batchId={}",id);
                        throw new BatchNameAlreadyExistException("Batch name already exists.");
                    }
                });


        mapper.updateEntity(request, batch);
        batch.setAvailableSeats(request.getMaxSeats());

        Batch updatedBatch = batchRepository.save(batch);

        log.info(
                "Batch updated successfully. batchId={}, batchCode={}, batchName={}",
                updatedBatch.getId(),
                updatedBatch.getBatchCode(),
                updatedBatch.getBatchName()
        );

        BatchResponse response = mapper.toResponseDTO(updatedBatch);

        return  response;

    }


    @Override
    public BatchResponse patchBatch(Long id, BatchPatchRequest request) {

        log.info("patching batch with id={}", id);

        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to patch batch. Batch not found. batchId={}",id);
                    return new BatchNotFoundException("Batch not found with id " + id);
                });


        batchRepository.findByBatchCode(request.getBatchCode())
                .ifPresent(existingBatch -> {
                    if (!existingBatch.getId().equals(id)) {
                        log.warn(
                                "Batch code already exists. batchId={}, batchCode={}",
                                id,
                                request.getBatchCode()
                        );
                        throw new BatchCodeAlreadyExistException("Batch code already exists");
                    }
                });

        batchRepository.findByBatchName(request.getBatchName())
                .ifPresent(existingBatch -> {
                    if (!existingBatch.getId().equals(id)) {

                        log.warn(
                                "Batch name already exists. batchId={}, batchName={}",
                                id,
                                request.getBatchName()
                        );
                        throw new BatchNameAlreadyExistException("Batch name already exists.");
                    }
                });



        mapper.patchEntity(request, batch);

        Batch updatedBatch = batchRepository.save(batch);

        log.info("Batch updated successfully. batchId={}", id);

        BatchResponse response = mapper.toResponseDTO(updatedBatch);

        return response;
    }

    @Override
    public void deleteBatch(Long id) {

        log.info("Deleting batch with id={}", id);

        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Batch not found for deletion. batchId={}", id);
                    return new BatchNotFoundException(
                            "Batch not found with id " + id
                    );
                });
        batchRepository.delete(batch);
        log.info("Batch deleted successfully. batchId={}", id);

    }


    public List<BatchResponse> getBatchesByIds(List<Long> ids) {
        List<Batch> batches = batchRepository.findAllById(ids);
        return mapper.toResponseDTOList(batches);
    }


}
