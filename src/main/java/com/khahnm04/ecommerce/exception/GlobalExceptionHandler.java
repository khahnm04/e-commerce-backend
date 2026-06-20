package com.khahnm04.ecommerce.exception;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.response.ErrorResponse;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.Objects;

@Slf4j(topic = "GLOBAL-EXCEPTION-HANDLER")
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Translator translator;

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ErrorResponse> handleRuntimeException(
        Exception exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        String message = translator.toLocale(errorCode.name(), errorCode.getMessage());
        log.warn("Uncategorized exception at {}: {}", request.getRequestURI(), message, exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
            .message(message)
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse> handleAppException(
        AppException exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();
        String message = translator.toLocale(errorCode.name(), errorCode.getMessage());
        log.warn("Uncategorized exception at {}: {} - [{}]", request.getRequestURI(), message, exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(message)
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    /*
     * Catch exception when Access Denied
     */
    @ExceptionHandler({
        AccessDeniedException.class,
        AuthorizationDeniedException.class
    })
    ResponseEntity<ErrorResponse> handlingAccessDeniedException(
        Exception exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        String message = translator.toLocale(errorCode.name(), errorCode.getMessage());
        log.warn("Access denied at {}: {} - [{}]", request.getRequestURI(), message, exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(message)
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    /*
     * Catch exception when validate
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        FieldError fieldError = Objects.requireNonNull(exception.getFieldError());
        String enumKey = fieldError.getDefaultMessage();
        ErrorCode errorCode = resolveErrorCode(enumKey);

        ConstraintViolation<?> constraintViolation = fieldError.unwrap(ConstraintViolation.class);
        Map<String, Object> attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        String translatedMessage = translator.toLocale(errorCode.name(), errorCode.getMessage());
        String finalMessage = Objects.nonNull(attributes)
            ? mapAttribute(translatedMessage, attributes)
            : translatedMessage;
        log.warn("Validation failed at {}: {} - [{}]", request.getRequestURI(), finalMessage, exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(finalMessage)
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /*
     * Catch exception when duplicate entry
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> handlingDataIntegrityViolationException(
        DataIntegrityViolationException exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_ENTRY;
        String message = translator.toLocale(errorCode.name(), errorCode.getMessage());
        log.warn("Data integrity violation at {}: {} - [{}]", request.getRequestURI(), message, exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(message)
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /*
     * Catch exception when request invalid
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handlingHttpMessageNotReadableException(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        String message = translator.toLocale(errorCode.name(), errorCode.getMessage());
        log.warn("Invalid request body at {}: {} - [{}]", request.getRequestURI(), message, exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(message)
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
