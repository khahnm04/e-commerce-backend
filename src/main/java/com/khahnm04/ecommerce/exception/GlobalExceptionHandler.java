package com.khahnm04.ecommerce.exception;

import com.khahnm04.ecommerce.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ErrorResponse> handlerRuntimeException(
        Exception exception,
        HttpServletRequest request
    ) {
        log.error("Uncategorized exception at {}: {}", request.getRequestURI(), exception.getMessage(), exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
            .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse> handlerAppException(
        AppException exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();
        String message = errorCode.getMessage();

        log.error("Uncategorized exception at {}: {}", request.getRequestURI(), message, exception);
        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(message)
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        FieldError fieldError = Objects.requireNonNull(exception.getFieldError());
        String enumKey = fieldError.getDefaultMessage();
        ErrorCode errorCode = resolveErrorCode(enumKey);

        ConstraintViolation<?> constraintViolation = fieldError.unwrap(ConstraintViolation.class);
        Map<String, Object> attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(Objects.nonNull(attributes)
                    ? mapAttribute(errorCode.getMessage(), attributes)
                    : errorCode.getMessage())
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private ErrorCode resolveErrorCode(String enumKey) {
        try {
            return ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ignored) {
            return ErrorCode.INVALID_KEY;
        }
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (Objects.nonNull(value)) {
                message = message.replace("{" + key + "}", String.valueOf(value));
            }
        }
        return message;
    }

}
