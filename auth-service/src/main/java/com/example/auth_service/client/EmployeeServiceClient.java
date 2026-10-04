package com.example.auth_service.client;

import com.example.auth_service.dto.internal.CreateEmployeeInternalRequest;
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
public class EmployeeServiceClient {
    private final RestClient restClient;
    private static final    Logger log = LoggerFactory.getLogger(EmployeeServiceClient.class);


    public EmployeeServiceClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://employee-service")
                .build();
    }

    public void createEmployee(CreateEmployeeInternalRequest request) {

        log.debug("Calling Employee Service to create employee");

        try {

            restClient.post()
                    .uri("/employee/internal")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            log.debug("Employee created successfully through Employee Service");

        } catch (HttpClientErrorException.NotFound ex) {

            log.warn("Employee Service returned not found while creating employee");

            throw new ResourceNotFoundException("Employee not found");
        }
        catch (ResourceAccessException ex) {

            log.warn(
                    "Employee Service is unavailable while creating employee. message={}",
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Employee service is unavailable");
        }
        catch (HttpServerErrorException ex) {

            log.error(
                    "Employee Service returned server error while creating employee. " +
                            "status={}, message={}",
                    ex.getStatusCode(),
                    ex.getMessage()
            );

            throw new ServiceUnavailableException("Employee service returner Server Error");
        }
    }
}
