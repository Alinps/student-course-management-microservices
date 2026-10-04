package com.example.course_assignment_service.client;


import com.example.course_assignment_service.dto.CourseTechnologyResponse;
import com.example.course_assignment_service.dto.IdsRequest;
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
public class CourseTechnologyServiceClient {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(CourseTechnologyServiceClient.class);


    public CourseTechnologyServiceClient(RestClient.Builder builder)  {
        this.restClient = builder
                .baseUrl("http://course-service")
                .build();
    }


    @Retry(name =   "courseService")
    @CircuitBreaker(
            name="courseService",
            fallbackMethod = "getCourseTechnologyFallback"
    )
    public CourseTechnologyResponse getCourseTechnology(Long courseTechnologyId) {

        log.debug(
                "Calling Course Service to fetch course technology. courseTechnologyId={}",
                courseTechnologyId
        );

        try {

            return restClient.get()
                    .uri("/course/internal/{id}", courseTechnologyId)
                    .retrieve()
                    .body(CourseTechnologyResponse.class);
        }

        catch (HttpClientErrorException.NotFound ex) {

            log.warn("Course Technology not found in Course Service. courseTechnologyId={}", courseTechnologyId);

            throw new ResourceNotFoundException("course not found with id:" + courseTechnologyId);
        }

        catch (ResourceAccessException ex) {

            log.error(
                    "Course Service is unavailable. courseTechnologyId={}, message={}",
                    courseTechnologyId,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Course service unavailable");
        }

        catch (HttpServerErrorException ex) {

            log.error(
                    "Course Service returned server error. " +
                            "courseTechnologyId={}, status={}, message={}",
                    courseTechnologyId,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Course service returned server error");
        }

    }


    @Retry(name = "courseService")
    @CircuitBreaker(
            name = "courseService",
            fallbackMethod = "getCourseTechnologiesFallback"
    )
    public List<CourseTechnologyResponse> getCourseTechnologiesByIds(List<Long> ids) {

        log.debug(
                "Calling Course Service to fetch courseTechnologies by ids. ids={}",
                ids
        );

        IdsRequest request = new IdsRequest(ids);

        try{
            List<CourseTechnologyResponse> responses =  restClient.post()
                    .uri("/course/internal/by-ids")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CourseTechnologyResponse>>() {});

            log.debug(
                    "Successfully fetched {} course technologies from Course Service",
                    responses != null ? responses.size() : 0
            );
            return responses;
        }

        catch (ResourceAccessException ex) {

            log.error(
                    "Course Service is unavailable. ids={},message={}",
                    ids,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Course service unavailable");
        }

        catch (HttpServerErrorException ex) {

            log.error(
                    "Course Service returned server error. " +
                            "Ids={}, status={}, message={}",
                    ids,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Course service returned server error");
        }

    }


    public CourseTechnologyResponse getCourseTechnologyFallback(Long courseTechnologyId, Throwable throwable) {

        if (throwable instanceof ResourceNotFoundException exception) {

            log.warn(
                    "Course technology not found. courseTechnologyId={}",
                    courseTechnologyId
            );

            throw exception;
        }

        log.error(
                "Course Service unavailable. Circuit breaker fallback executed. " +
                        "courseTechnologyId={}, cause={}",
                courseTechnologyId,
                throwable.getMessage()
        );

        throw new ServiceUnavailableException("Course service is currently unavailable"

        );
    }

    public List<CourseTechnologyResponse> getCourseTechnologiesFallback(List<Long> ids, Throwable throwable) {
        log.error(
                "Course Service unavailable. Circuit breaker fallback executed. " +
                        "courseTechnologyIds={}, cause={}",
                ids,
                throwable.getMessage()
        );

        throw new ServiceUnavailableException("Course service is currently unavailable");
    }
}
