package com.khahnm18.ecommercebackend.exception;

import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
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
public class GlobalExceptionHandler {

    private final Translator translator;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneral(
        Exception ex,
        HttpServletRequest request
    ) {
        log.error("Unexpected error at {}", request.getRequestURI(), ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            translator.toLocale("UNCATEGORIZED_EXCEPTION")
        );
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("https://ecommerce.com/errors/internal-error"));
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.internalServerError().body(problem);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ProblemDetail> handleAppException(
        AppException ex,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("Business exception at {}: {}", request.getRequestURI(), errorCode.getMessage());

        String title = Arrays.stream(errorCode.name().split("_"))
            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));

        URI type = URI.create(
            "https://ecommerce.com/errors/" +
                errorCode.name().toLowerCase().replace("_", "-")
        );

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            errorCode.getStatusCode(),
            translator.toLocale(errorCode.name(), errorCode.getMessage())
        );

        problem.setTitle(title);
        problem.setType(type);
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errorCode", errorCode.name());

        return ResponseEntity.status(errorCode.getStatusCode()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        log.warn("Validation failed at {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            translator.toLocale("VALIDATION_FAILED")
        );
        problem.setTitle("Validation Failed");
        problem.setType(URI.create("https://ecommerce.com/errors/validation-failed"));
        problem.setInstance(URI.create(request.getRequestURI()));

        Map<String, String> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> translator.toLocale(fieldError.getDefaultMessage()),
                (first, second) -> first
            ));
        problem.setProperty("fieldErrors", fieldErrors);

        return ResponseEntity.badRequest().body(problem);
    }

}
