package com.khahnm18.ecommercebackend.security;

import com.khahnm18.ecommercebackend.exception.ErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.http.ProblemDetail;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.URI;

@Slf4j(topic = "JWT-ACCESS-DENIED-HANDLER")
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

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
        problem.setProperty("errorCode", errorCode.name());

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(problem));
        response.flushBuffer();
    }

}
