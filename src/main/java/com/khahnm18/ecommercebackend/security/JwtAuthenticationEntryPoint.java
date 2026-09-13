package com.khahnm18.ecommercebackend.security;

import com.khahnm18.ecommercebackend.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.http.MediaType;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.URI;

@Slf4j(topic = "JWT-AUTHENTICATION-ENTRY-POINT")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        log.warn("Authentication failed at {}: {}", request.getRequestURI(), errorCode.getMessage());

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
