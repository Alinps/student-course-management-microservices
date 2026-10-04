package com.example.note_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "X-Correlation-ID";

    private static final String MDC_KEY = "correlationId";

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId = request.getHeader(HEADER_NAME);

        if (correlationId == null || correlationId.isBlank()) {

            correlationId = UUID.randomUUID().toString();

            log.debug("Generated new correlation ID={}", correlationId);
        }

        /*
        MDC = Mapped Diagnostic Context.
        It is a logging feature provided by SLF4J/Logback that lets you attach contextual information to the current request/thread.
        Think of it like a temporary storage associated with the current request.
         */
        MDC.put(MDC_KEY, correlationId);

        response.setHeader(
                HEADER_NAME,
                correlationId
        );

        try {
            /*
            "Finished filter's processing. Continue processing this request through
            the remaining filters and eventually the controller."
            Without this line, the request generally would not continue to the controller.
             */
            filterChain.doFilter(request, response);
        }
        finally {
            /*
            because server threads are reused.
            This is very important.
            Suppose Spring has a thread:http-nio-8080-exec-1

            Request 1 uses it: Request A correlationId = ABC-123

            You put:
            MDC.put("correlationId", "ABC-123");

            When Request A finishes, the thread doesn't necessarily disappear.
            Spring may reuse the same thread for Request B:

            If you don't remove the MDC value, there is a risk that stale contextual data
            remains associated with the reused thread.

            So you clean it:
            MDC.remove("correlationId");
            Now the thread is clean for the next request.
             */
            MDC.remove(MDC_KEY);
        }
    }
}


