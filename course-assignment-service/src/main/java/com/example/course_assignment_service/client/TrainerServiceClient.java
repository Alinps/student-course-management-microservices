package com.example.course_assignment_service.client;

import com.example.course_assignment_service.dto.IdsRequest;
import com.example.course_assignment_service.dto.TrainerResponse;
import com.example.course_assignment_service.exception.ResourceNotFoundException;
import com.example.course_assignment_service.exception.ServiceUnavailableException;
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
public class TrainerServiceClient {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceClient.class);



    public TrainerServiceClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://employee-service")
                .build();
    }

    @Retry(name="trainerService")
    @CircuitBreaker(
            name="trainerService",
            fallbackMethod = "getTrainerFallback"
    )
    public TrainerResponse getEmployee(Long trainerId) {

        log.debug(
                "Calling Employee Service to fetch trainer. trainerId={}",
                trainerId
        );



        try {

            return restClient.get()
                    .uri("/employee/internal/{id}", trainerId)
                    .retrieve() // This is the part that actually sends the HTTP request.
                    .body(TrainerResponse.class); // Take the JSON response and create an object of type EmployeeResponse
        }

        catch (HttpClientErrorException.NotFound ex) {

            log.warn(
                    "Trainer not found in Employee Service. trainerId={}",
                    trainerId
            );

            throw new ResourceNotFoundException("Employee not found with id: " + trainerId);
        }

        catch (ResourceAccessException ex) {


            log.warn(
                    "Employee Service is unavailable. trainerId={}, message={}",
                    trainerId,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Employee service unavailable");
        }

        catch (HttpServerErrorException ex) {

            log.error(
                    "Employee Service returned server error. " +
                            "trainerId={}, status={}, message={}",
                    trainerId,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Employee service returned server error.");
        }

    }

    @Retry(name="trainerService")
    @CircuitBreaker(
            name="trainerService",
            fallbackMethod = "getTrainersFallback"
    )
    public List<TrainerResponse> getTrainerByIds(List<Long> ids) {
        IdsRequest request = new IdsRequest(ids);

        try {
            return restClient.post()
                    .uri("/employee/internal/by-ids")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<TrainerResponse>>() {});
        }  catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Employee service unavailable");
        } catch (HttpServerErrorException ex) {
            throw new ServiceUnavailableException("Employee service returned server error.");
        }

    }

    public TrainerResponse getTrainerFallback(Long trainerId,Throwable throwable) {

        if (throwable instanceof ResourceNotFoundException exception) {

            log.warn(
                    "Trainer not found. Circuit breaker fallback received " +
                            "ResourceNotFoundException. trainerId={}",
                    trainerId
            );


            throw exception;
        }

        log.error(
                "Employee Service unavailable. Circuit breaker fallback executed. " +
                        "trainerId={}, cause={}",
                trainerId,
                throwable.getMessage()
        );

        throw new ServiceUnavailableException("Employee service is currently unavailable");
    }

    public List<TrainerResponse> getTrainersFallback(List<Long> ids, Throwable throwable) {
        log.error(
                "Employee Service unavailable. Circuit breaker fallback executed. " +
                        "cause={}",
                throwable.getMessage()
        );
        throw new ServiceUnavailableException("Employee service is currently unavailable");
    }


}
