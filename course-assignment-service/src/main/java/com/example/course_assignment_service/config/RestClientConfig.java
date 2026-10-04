package com.example.course_assignment_service.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.autoconfigure.RestClientBuilderConfigurer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

//@Configuration
//public class RestClientConfig {
//
//    @Bean
//    @LoadBalanced //@LoadBalanced annotation is what tells Spring to apply the load-balancing interceptor to the client
//    public RestClient.Builder restClientBuilder() {
//        return RestClient.builder();
//    }
//
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
    public RestClient.Builder restClientBuilder(RestClientBuilderConfigurer configurer) {

        return  configurer
                .configure(RestClient.builder())
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