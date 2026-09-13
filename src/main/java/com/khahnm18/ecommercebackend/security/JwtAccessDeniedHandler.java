package com.khahnm18.ecommercebackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import java.io.IOException;
import java.net.URI;

@Slf4j(topic = "JWT-ACCESS-DENIED-HANDLER")
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        log.warn("Authorization failed at {}: {}", request.getRequestURI(), errorCode.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            errorCode.getStatusCode(),
            errorCode.getMessage()
        );

        problem.setTitle(errorCode.name());
        problem.setType(
            URI.create(
                "https://ecommerce.com/errors/" +
                    errorCode.name().toLowerCase().replace("_", "-")
            )
        );
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("code", errorCode.name());

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        response.getWriter().write(objectMapper.writeValueAsString(problem));
        response.flushBuffer();
    }

}
