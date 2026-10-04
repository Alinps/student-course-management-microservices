package com.example.note_service.client;

import com.example.note_service.dto.response.TechnologyResponse;
import com.example.note_service.exception.ResourceNotFoundException;
import com.example.note_service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;


@Service
public class CourseServiceClient {
    private final RestClient restClient;
    private static final Logger log =  LoggerFactory.getLogger(CourseServiceClient.class);



    public CourseServiceClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://course-service")
                .build();

    }


    @Retry(name = "courseService")
    @CircuitBreaker(
            name = "courseService",
            fallbackMethod = "getCourseFallBack"
    )
    public TechnologyResponse getTechnology(Long technologyId) {

        log.debug("Calling Course Service to fetch technology. technologyId={}", technologyId);

        try {

            return restClient.get()
                    .uri(
                            "/course/technologies/internal/{id}",
                            technologyId
                    )
                    .retrieve()
                    .body(TechnologyResponse.class);

        } catch (HttpClientErrorException.NotFound ex) {

            log.warn("Technology not found in Course Service. technologyId={}", technologyId);

            throw new ResourceNotFoundException("Technology not found");

        } catch (ResourceAccessException ex) {

            log.warn(
                    "Course Service is unavailable while fetching technology. " +
                            "technologyId={}, message={}",
                    technologyId,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Course Service is currently unavailable");

        } catch (HttpServerErrorException ex) {

            log.error(
                    "Course Service returned server error while fetching technology. " +
                            "technologyId={}, status={}, message={}",
                    technologyId,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Course Service returned Server Error");
        }
    }


    public TechnologyResponse getCourseFallBack(
            Long technologyId,
            Throwable throwable) {

        if (throwable instanceof ResourceNotFoundException exception) {

            log.warn(
                    "Technology not found. Circuit breaker fallback received " +
                            "ResourceNotFoundException. technologyId={}",
                    technologyId
            );

            throw exception;
        }

        log.error(
                "Course Service unavailable. Circuit breaker fallback executed. " +
                        "technologyId={}, cause={}",
                technologyId,
                throwable.getMessage()
        );

        throw new ServiceUnavailableException("Course Service is currently unavailable");
    }


}
