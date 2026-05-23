package com.khahnm04.ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY("Invalid key error code", HttpStatus.BAD_REQUEST),
    BULK_ACTION_IDS_REQUIRED("Ids is required", HttpStatus.BAD_REQUEST),
    BULK_ACTION_REQUIRED("action is required", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT("Invalid date format", HttpStatus.BAD_REQUEST),
    LIST_PAGE_MIN("Page must be >= {value}", HttpStatus.BAD_REQUEST),
    LIST_SIZE_MIN("Size must be >= {value}", HttpStatus.BAD_REQUEST),
    LIST_SIZE_MAX("Size must be <= {value}", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("You do not have permission", HttpStatus.FORBIDDEN),
    REFRESH_TOKEN_FAILED( "Refresh token failed", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_REQUIRED( "Refresh token  is required", HttpStatus.UNAUTHORIZED),
    TOKEN_CREATION_FAILED( "Token creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_IDENTIFIER_REQUIRED("Missing login identifier", HttpStatus.BAD_REQUEST),
    LOGIN_PHONE_NUMBER_REQUIRED("Login phone number is required", HttpStatus.BAD_REQUEST),
    LOGIN_EMAIL_REQUIRED("Login email is required", HttpStatus.BAD_REQUEST),
    LOGIN_PASSWORD_REQUIRED("Login password is required", HttpStatus.BAD_REQUEST),

    ROLE_CODE_REQUIRED("Role code is required", HttpStatus.BAD_REQUEST),
    ROLE_NAME_REQUIRED("Role name is required", HttpStatus.BAD_REQUEST),
    ROLE_DESCRIPTION_REQUIRED("Role description is required", HttpStatus.BAD_REQUEST),
    ROLE_PERMISSIONS_REQUIRED("Role permissions are required", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED("Role already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND("Role not found", HttpStatus.NOT_FOUND),

    PERMISSION_REQUIRED("Permission cannot be blank", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND("Permission not found", HttpStatus.BAD_REQUEST),

    CUSTOMER_NOT_FOUND("Customer not found", HttpStatus.NOT_FOUND),

    EMPLOYEE_NOT_FOUND("Employee not found", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.code = this.name();
        this.message = message;
        this.statusCode = statusCode;
    }

}
