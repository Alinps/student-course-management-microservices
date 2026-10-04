package com.example.batch_service.repository;

import com.example.batch_service.enums.BatchStatus;
import com.example.batch_service.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findByStatus(BatchStatus status);
    Optional<Batch> findByBatchName(String batchName);
    Optional<Batch> findByBatchCode(String batchCode);
    boolean existsByBatchCode(String batchCode);
    boolean existsByBatchName(String batchName);

}
