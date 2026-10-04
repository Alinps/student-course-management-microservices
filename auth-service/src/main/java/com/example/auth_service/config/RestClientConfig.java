package com.example.auth_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;

//@Configuration
//public class RestClientConfig {
//
//    @Bean
//    @LoadBalanced
//    public RestClient.Builder restClientBuilder() {
//        return RestClient.builder();
//    }
//
//    @Bean
//    public RestClient restClient(RestClient.Builder restClientBuilder) {
//        return restClientBuilder.build();
//    }
//}


@Configuration
public class RestClientConfig {

    @Value("${internal.auth.service-name}")
    private String serviceName;

    @Value("${internal.auth.service-key}")
    private String serviceKey;

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {

        return RestClient.builder()
                .requestInterceptor(
                        (request, body, execution) -> {

                    // Service-to-service authentication
                    request.getHeaders().set("X-Service-Name", serviceName);

                    request.getHeaders().set("X-Service-Key", serviceKey);

                    // Correlation ID propagation
                    String correlationId = MDC.get("correlationId");

                    if (correlationId != null && !correlationId.isBlank()) {
                        request.getHeaders().set("X-Correlation-ID", correlationId);
                    }

                    return execution.execute(request, body);
                });
    }

    @Bean
    public RestClient restClient(
            RestClient.Builder restClientBuilder) {

        return restClientBuilder.build();
    }
}