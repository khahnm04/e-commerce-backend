package com.khahnm04.ecommerce.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.khahnm04.ecommerce.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ErrorResponse> handleRuntimeException(
        Exception exception,
        HttpServletRequest request
    ) {
        String message = resolveErrorCodeMessage(ErrorCode.UNCATEGORIZED_EXCEPTION);
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
        String message = resolveErrorCodeMessage(errorCode);
        log.warn("Uncategorized exception at {}: {}", request.getRequestURI(), message, exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(message)
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

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

        String translatedMessage = resolveErrorCodeMessage(errorCode);
        String finalMessage = Objects.nonNull(attributes)
            ? mapAttribute(translatedMessage, attributes)
            : translatedMessage;

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(finalMessage)
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Catch exception when Duplicate entry
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> handlingDataIntegrityViolationException(
        DataIntegrityViolationException exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_ENTRY;
        String message = resolveErrorCodeMessage(errorCode);
        log.warn("Data integrity violation at {}: {}", request.getRequestURI(), message, exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(message)
            .path(request.getRequestURI())
            .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Catch exception when request invalid
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handlingHttpMessageNotReadableException(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode;
        Throwable cause = exception.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            if (invalidFormat.getTargetType() != null && invalidFormat.getTargetType().isEnum()) {
                errorCode = ErrorCode.ENUM_INVALID;
            } else {
                errorCode = ErrorCode.INVALID_DATE_FORMAT;
            }
        } else {
            errorCode = ErrorCode.INVALID_REQUEST_BODY;
        }
        String message = resolveErrorCodeMessage(errorCode);
        log.warn("Invalid request body at {}: {}", request.getRequestURI(), exception.getMessage());

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

    private String resolveErrorCodeMessage(ErrorCode errorCode) {
        return messageSource.getMessage(
            errorCode.name(),
            null,
            errorCode.getMessage(),
            LocaleContextHolder.getLocale()
        );
    }

}
