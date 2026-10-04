package com.example.student_batch_assignment_service.client;

import com.example.student_batch_assignment_service.dto.BatchResponse;
import com.example.student_batch_assignment_service.dto.BatchStudentResponse;
import com.example.student_batch_assignment_service.dto.IdsRequest;
import com.example.student_batch_assignment_service.exception.ResourceNotFoundException;
import com.example.student_batch_assignment_service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

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
    public BatchResponse getBatch(Long id) {

        log.debug("Calling Batch Service to fetch batch. batchId={}", id);


        try {

            return restClient.get()
                    .uri("/batch/internal/{id}",id)
                    .retrieve()
                    .body(BatchResponse.class);
        }

        catch (HttpClientErrorException.NotFound ex) {

            log.warn("Batch not found in Batch Service. batchId={}", id);
            throw new ResourceNotFoundException("Batch not found");
        }
        catch (ResourceAccessException ex) {

            log.error(
                    "Batch Service is unavailable. batchId={}, message={}",
                    id,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Batch service is unavailable currently");
        }
        catch (HttpServerErrorException ex) {

            log.error(
                    "Batch Service returned server error. " +
                            "batchId={}, status={}, message={}",
                    id,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Batch service returned server error");
        }
    }


    @Retry(name="batchService")
    @CircuitBreaker(
            name="batchService",
            fallbackMethod = "getBatchesFallback"
    )
    public List<BatchResponse> getBatchesByIds(List<Long> ids) {

        try {

            IdsRequest request = new IdsRequest(ids);
            List<BatchResponse> responses =  restClient.post()
                    .uri("/batch/internal/by-ids")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<BatchResponse>>() {});
            log.debug(
                    "Successfully fetched {} batches from Batch Service",
                    responses != null ? responses.size() : 0
            );

            return responses;

        }  catch (ResourceAccessException ex) {

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





    public BatchStudentResponse getBatchFallback(Long id, Throwable throwable) {
        if (throwable instanceof ResourceNotFoundException exception) {

            log.warn("Batch not found. Circuit breaker fallback received " +
                    "ResourceNotFoundException. batchId={}", id);

            throw exception;
        }

        log.error(
                "Batch Service unavailable. Circuit breaker fallback executed. " +
                        "batchId={}, cause={}",
                id,
                throwable.getMessage()
        );

        throw new ServiceUnavailableException("Batch service is currently unavailable");
    }

    public List<BatchStudentResponse> getBatchesFallback(List<Long> ids, Throwable throwable) {

        log.error(
                "Batch Service unavailable. Circuit breaker fallback executed. " +
                        "batchIds={}, cause={}",
                ids,
                throwable.getMessage()
        );


        throw new ServiceUnavailableException("Batch service is currently unavailable");
    }



}
