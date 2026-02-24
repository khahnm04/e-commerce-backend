package com.khahnm04.ecommerce.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.khahnm04.ecommerce.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catch exception when Duplicate entry
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> handlingDataIntegrityViolationException(DataIntegrityViolationException exception,
            HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_ENTRY;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ErrorResponse> handlingRuntimeException(RuntimeException exception, HttpServletRequest request) {
        log.error("Uncategorized exception at {}: {}", request.getRequestURI(), exception.getMessage(), exception);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse> handlingAppException(AppException exception, HttpServletRequest request) {
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(value = org.springframework.web.bind.MissingRequestCookieException.class)
    ResponseEntity<ErrorResponse> handlingMissingRequestCookieException(
            org.springframework.web.bind.MissingRequestCookieException exception, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.VALIDATION_EXCEPTION;
        if (com.khahnm04.ecommerce.common.constant.TokenConstants.REFRESH_TOKEN.equals(exception.getCookieName())) {
            errorCode = ErrorCode.REFRESH_TOKEN_FAILED;
        } else if (com.khahnm04.ecommerce.common.constant.TokenConstants.ACCESS_TOKEN
                .equals(exception.getCookieName())) {
            errorCode = ErrorCode.UNAUTHENTICATED;
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    // Catch exception when Access Denied
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ErrorResponse> handlingAccessDeniedException(AccessDeniedException exception,
            HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(value = ServiceValidationException.class)
    ResponseEntity<ErrorResponse> handlingServiceValidationException(ServiceValidationException exception,
            HttpServletRequest request) {

        List<ErrorResponse.FieldErrorResponse> errors = exception.getErrors().entrySet()
                .stream()
                .map(entry -> ErrorResponse.FieldErrorResponse.builder()
                        .field(entry.getKey())
                        .message(entry.getValue())
                        .build())
                .toList();

        ErrorCode errorCode = ErrorCode.VALIDATION_EXCEPTION;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Catch exception when enum invalid
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handlingHttpMessageNotReadableException(HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        String message = "Invalid JSON format";
        if (exception.getCause() instanceof InvalidFormatException ife) {
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                message = String.format("Invalid value '%s' for enum %s", ife.getValue(),
                        ife.getTargetType().getSimpleName());
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.VALIDATION_EXCEPTION.getCode())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Catch exception when Validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handlingValidation(MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        List<ErrorResponse.FieldErrorResponse> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> {
                    String enumKey = fieldError.getDefaultMessage();
                    ErrorCode errorCode;
                    try {
                        errorCode = ErrorCode.valueOf(enumKey);
                    } catch (IllegalArgumentException | NullPointerException e) {
                        errorCode = null;
                    }

                    String message = resolveMessage(errorCode, fieldError, enumKey);

                    return ErrorResponse.FieldErrorResponse.builder()
                            .field(fieldError.getField())
                            .code(errorCode != null ? errorCode.getCode() : ErrorCode.VALIDATION_EXCEPTION.getCode())
                            .message(message)
                            .build();
                })
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.VALIDATION_EXCEPTION.getCode())
                .message(ErrorCode.VALIDATION_EXCEPTION.getMessage())
                .path(request.getRequestURI())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    private String resolveMessage(ErrorCode errorCode, FieldError fieldError, String fallbackMessage) {
        if (errorCode == null) {
            return fallbackMessage;
        }

        String message = errorCode.getMessage();
        try {
            ConstraintViolation<?> constraintViolation = fieldError.unwrap(ConstraintViolation.class);
            Map<String, Object> attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        } catch (Exception ignored) {
        }
        return message;
    }

}
