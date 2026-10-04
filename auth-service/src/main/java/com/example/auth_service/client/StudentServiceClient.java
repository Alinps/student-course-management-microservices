package com.example.auth_service.client;

import com.example.auth_service.dto.internal.CreateStudentInternalRequest;
import com.example.auth_service.exception.ResourceNotFoundException;
import com.example.auth_service.exception.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;



@Component
public class StudentServiceClient {
    private final RestClient restClient;
    private static final Logger log =  LoggerFactory.getLogger(StudentServiceClient.class);


    public StudentServiceClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://student-service")
                .build();
    }


    public void createStudent(CreateStudentInternalRequest request){

        log.debug("Calling Student Service to create employee");

        try {

            restClient.post()
                    .uri("/student/internal")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            log.debug("Student created successfully through Student Service");


        }  catch (HttpClientErrorException.NotFound ex) {

            log.warn(
                    "Student Service returned not found while creating student"
            );
            throw new ResourceNotFoundException("Student not found");
        }
        catch (ResourceAccessException ex) {

            log.warn(
                    "Student Service is unavailable while creating student. message={}",
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Student service is unavailable");
        }
        catch (HttpServerErrorException ex) {

            log.error(
                    "Student Service returned server error while creating student. " +
                            "status={}, message={}",
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Student service returner Server Error");
        }
    }
}
