package com.khahnm18.ecommercebackend.exception;

import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ProblemDetail;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Map;
import java.net.URI;

@Slf4j(topic = "GLOBAL-EXCEPTION-HANDLER")
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private final Translator translator;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneral(
        Exception ex,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        log.error("Unexpected error at {}", request.getRequestURI(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            errorCode.getStatusCode(),
            translator.toLocale(errorCode.name(), errorCode.getMessage())
        );
        problem.setTitle(buildTitle(errorCode.name()));
        problem.setType(URI.create(
            "https://ecommerce.com/errors/" +
                errorCode.name().toLowerCase().replace("_", "-")
        ));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errorCode", errorCode.name());
        return ResponseEntity.internalServerError().body(problem);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ProblemDetail> handleAppException(
        AppException ex,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("Business exception at {}: {}", request.getRequestURI(), errorCode.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            errorCode.getStatusCode(),
            translator.toLocale(errorCode.name(), errorCode.getMessage())
        );
        problem.setTitle(buildTitle(errorCode.name()));
        problem.setType(URI.create(
            "https://ecommerce.com/errors/" +
                errorCode.name().toLowerCase().replace("_", "-")
        ));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errorCode", errorCode.name());
        return ResponseEntity.status(errorCode.getStatusCode()).body(problem);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationDenied(
        AccessDeniedException ex,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        log.warn("Authorization failed at {}: {}", request.getRequestURI(), errorCode.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            errorCode.getStatusCode(),
            translator.toLocale(errorCode.name(), errorCode.getMessage())
        );
        problem.setTitle(buildTitle(errorCode.name()));
        problem.setType(URI.create(
            "https://ecommerce.com/errors/" +
                errorCode.name().toLowerCase().replace("_", "-")
        ));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errorCode", errorCode.name());
        return ResponseEntity.status(errorCode.getStatusCode()).body(problem);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ProblemDetail> handleRateLimitException(
        RateLimitException ex,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.TOO_MANY_REQUESTS;
        log.warn("Rate limit exceeded at {}: retry after {}s", request.getRequestURI(), ex.getRetryAfterSeconds());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            errorCode.getStatusCode(),
            translator.toLocale(errorCode.name(), errorCode.getMessage())
        );
        problem.setTitle(buildTitle(errorCode.name()));
        problem.setType(URI.create(
            "https://ecommerce.com/errors/" +
                errorCode.name().toLowerCase().replace("_", "-")
        ));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errorCode", errorCode.name());
        problem.setProperty("retryAfterSeconds", ex.getRetryAfterSeconds());
        return ResponseEntity.status(errorCode.getStatusCode())
            .header("Retry-After", String.valueOf(ex.getRetryAfterSeconds()))
            .body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        log.warn("Validation failed at {}: {}", request.getRequestURI(), ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            errorCode.getStatusCode(),
            translator.toLocale(errorCode.name(), errorCode.getMessage())
        );
        problem.setTitle(buildTitle(errorCode.name()));
        problem.setType(URI.create(
            "https://ecommerce.com/errors/" +
                errorCode.name().toLowerCase().replace("_", "-")
        ));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errorCode", errorCode.name());
        Map<String, String> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> translator.toLocale(fieldError.getDefaultMessage()),
                (first, second) -> first
            ));
        problem.setProperty("fieldErrors", fieldErrors);
        return ResponseEntity.status(errorCode.getStatusCode()).body(problem);
    }

    private String buildTitle(String errorCodeName) {
        return Arrays.stream(errorCodeName.split("_"))
            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));
    }

}
