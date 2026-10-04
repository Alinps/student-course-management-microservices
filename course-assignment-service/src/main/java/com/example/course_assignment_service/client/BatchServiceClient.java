package com.example.course_assignment_service.client;

import com.example.course_assignment_service.dto.BatchResponse;
import com.example.course_assignment_service.dto.CourseTechnologyResponse;
import com.example.course_assignment_service.dto.IdsRequest;
import com.example.course_assignment_service.exception.*;

import com.example.course_assignment_service.service.CourseAssignmentServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.ConnectException;
import java.util.List;

@Service
public class BatchServiceClient {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(BatchServiceClient.class);


    public BatchServiceClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://batch-service")
                .build();
    }

    @Retry(name = "batchService")
    @CircuitBreaker(
            name = "batchService",
            fallbackMethod = "getBatchFallback"
    )
    public BatchResponse getBatch(Long batchId) {


        log.debug(
                "Calling Batch Service to fetch batch. batchId={}",
                batchId
        );

        try {

            return restClient.get()
                    .uri("/batch/internal/{id}", batchId)
                    .retrieve()
                    .body(BatchResponse.class);

        }

        catch (HttpClientErrorException.NotFound ex) {

            log.warn(
                    "Batch not found in Batch Service. batchId={}",
                    batchId
            );

            throw new ResourceNotFoundException("Batch not found");
        }

        catch (ResourceAccessException ex) {

            log.error(
                    "Batch Service is unavailable. batchId={}, message={}",
                    batchId,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Batch Service is unavailable");
        }

        catch (HttpServerErrorException ex) {

            log.error(
                    "Batch Service returned server error. " +
                            "batchId={}, status={}, message={}",
                    batchId,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Batch Service returned server error");
        }


    }


    @Retry(name="batchService")
    @CircuitBreaker(
            name="batchService",
            fallbackMethod = "getBatchesFallback"
    )
    public List<BatchResponse> getBatchesByIds(List<Long> ids) {

        log.debug(
                "Calling Batch Service to fetch batches by ids. ids={}",
                ids
        );


        try {

            IdsRequest request = new IdsRequest(ids);
            List<BatchResponse> responses = restClient.post()
                    .uri("/batch/internal/by-ids")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<BatchResponse>>() {});

            log.debug(
                    "Successfully fetched {} batches from Batch Service",
                    responses != null ? responses.size() : 0
            );

            return responses;

        }
        catch (ResourceAccessException ex) {

            log.warn(
                    "Batch Service is unavailable while fetching batches. ids={}, message={}",
                    ids,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Batch Service is unavailable");

        } catch (HttpServerErrorException ex) {

            log.error(
                    "Batch Service returned server error while fetching batches. " +
                            "ids={}, status={}, message={}",
                    ids,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Batch Service returned server error");
        }

    }


    public BatchResponse getBatchFallback(
            Long batchId,
            Throwable throwable
    ) {

        if (throwable instanceof ResourceNotFoundException exception) {

            log.warn("Batch not found. batchId={}", batchId);
            throw exception;
        }

        log.error(
                "Batch Service unavailable. Circuit breaker fallback executed. " +
                        "batchId={}, cause={}",
                batchId,
                throwable.getMessage()
        );

        throw new ServiceUnavailableException("Batch service is currently unavailable");
    }

    public List<BatchResponse> getBatchesFallback(List<Long> ids, Throwable throwable) {

        log.error(
                "Batch Service unavailable. Circuit breaker fallback executed. " +
                        "batchIds={}, cause={}",
                ids,
                throwable.getMessage()
        );

        throw new ServiceUnavailableException("Batch service is currently unavailable");
    }
}
