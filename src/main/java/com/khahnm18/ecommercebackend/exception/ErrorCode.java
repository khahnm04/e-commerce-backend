package com.khahnm18.ecommercebackend.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_FAILED("The submitted data is invalid. Please check the fields", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("You do not have permission", HttpStatus.FORBIDDEN),
    TOKEN_GENERATION_FAILED("Token creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_FAILED("Login failed", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_FAILED( "Refresh token failed", HttpStatus.UNAUTHORIZED),

    ACCOUNT_NOT_FOUND("Account not found", HttpStatus.NOT_FOUND),

    ROLE_EXISTED("Role already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND("Role not found", HttpStatus.BAD_REQUEST),

    PERMISSION_NOT_FOUND("Permission not found", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
