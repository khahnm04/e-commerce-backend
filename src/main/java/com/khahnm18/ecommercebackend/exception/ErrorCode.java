package com.khahnm18.ecommercebackend.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_FAILED("The submitted data is invalid. Please check the fields", HttpStatus.BAD_REQUEST),
    INVALID_INSTANT_FORMAT("Invalid instant format", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("You do not have permission", HttpStatus.FORBIDDEN),
    TOKEN_GENERATION_FAILED("Token creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_FAILED("Login failed", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_FAILED( "Refresh token failed", HttpStatus.UNAUTHORIZED),

    PERMISSION_NOT_FOUND("Permission not found", HttpStatus.BAD_REQUEST),

    ROLE_EXISTED("Role already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND("Role not found", HttpStatus.BAD_REQUEST),

    ACCOUNT_EXISTED("Account already exists", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND("Account not found", HttpStatus.NOT_FOUND),

    OLD_PASSWORD_INCORRECT("Old password is incorrect", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_SAME_AS_OLD("New password must be different from the old password", HttpStatus.BAD_REQUEST),

    WEBSITE_INFO_NOT_FOUND("Website info not found", HttpStatus.NOT_FOUND),

    BRAND_EXISTED("Brand already exists", HttpStatus.BAD_REQUEST),
    BRAND_NOT_FOUND("Brand not found", HttpStatus.NOT_FOUND),

    CATEGORY_EXISTED("Category already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_PARENT_INVALID("The parent category is invalid", HttpStatus.BAD_REQUEST),

    PRODUCT_EXISTED("Product already exists", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("Product not found", HttpStatus.NOT_FOUND),
    ;

    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
