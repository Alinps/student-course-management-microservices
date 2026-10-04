package com.example.student_batch_assignment_service.client;

import com.example.student_batch_assignment_service.dto.BatchStudentResponse;
import com.example.student_batch_assignment_service.dto.IdsRequest;
import com.example.student_batch_assignment_service.dto.StudentResponse;
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
public class StudentServiceClient {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(StudentServiceClient.class);


    public StudentServiceClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://student-service")
                .build();
    }

    @Retry(name="studentService")
    @CircuitBreaker(
            name="studentService",
            fallbackMethod = "getStudentFallback"
    )
    public StudentResponse getStudent(Long id) {


        log.debug("Calling Student Service to fetch student. studentId={}", id);

        try {
            return restClient.get()
                    .uri("/student/{id}",id)
                    .retrieve()
                    .body(StudentResponse.class);

        }
        catch (HttpClientErrorException.NotFound ex) {

            log.warn("Student not found in Student Service. studentId={}", id   );

            throw new ResourceNotFoundException("Student not found");
        }
        catch (ResourceAccessException ex) {

            log.warn(
                    "Student Service is unavailable. studentId={}, message={}",
                    id,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Student service is unavailable");
        }
        catch (HttpServerErrorException ex) {

            log.error(
                    "Student Service returned server error. " +
                            "studentId={}, status={}, message={}",
                    id,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Student service returner Server Error");
        }

    }

    @Retry(name="studentService")
    @CircuitBreaker(
            name="studentService",
            fallbackMethod = "getStudentsFallback"
    )
    public List<StudentResponse> getStudentByIds(List<Long> ids) {

        log.debug("Calling Student Service to fetch students by ids. ids={}", ids);

        try {
            IdsRequest request = new IdsRequest(ids);
            List<StudentResponse> responses = restClient.post()
                    .uri("/student/internal/by-ids")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<StudentResponse>>() {});
            log.debug(
                    "Successfully fetched {} students from Student Service",
                    responses != null ? responses.size() : 0
            );

            return responses;

        } catch (ResourceAccessException ex) {

            log.warn(
                    "Student Service is unavailable while fetching students. " +
                            "ids={}, message={}",
                    ids,
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Student Service is unavailable");
        } catch (HttpServerErrorException ex) {

            log.error(
                    "Student Service returned server error while fetching students. " +
                            "ids={}, status={}, message={}",
                    ids,
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Student Service returned server error");
        }
    }


    public BatchStudentResponse getStudentFallback(Long id, Throwable throwable) {

        if (throwable instanceof ResourceAccessException exception) {

            log.warn(
                    "Student not found. Circuit breaker fallback received " +
                            "ResourceNotFoundException. studentId={}",
                    id
            );

            throw exception;
        }

        log.error(
                "Student Service unavailable. Circuit breaker fallback executed. " +
                        "studentId={}, cause={}",
                id,
                throwable.getMessage()
        );

        throw new  ServiceUnavailableException("Student service is unavailable");
    }

    public List<BatchStudentResponse> getStudentsFallback(List<Long> ids, Throwable throwable) {

        log.error(
                "Student Service unavailable. Circuit breaker fallback executed. " +
                        "studentIds={}, cause={}",
                ids,
                throwable.getMessage()
        );

        throw new  ServiceUnavailableException("Student service is unavailable");
    }
}
