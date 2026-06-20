package com.khahnm04.ecommerce.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_ENTRY("Duplicate entry", HttpStatus.BAD_REQUEST),
    INVALID_KEY("Invalid key error code", HttpStatus.BAD_REQUEST),
    INVALID_INPUT("Invalid request body", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT("Invalid date format", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT("Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_FORMAT("Invalid phone format", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_FORMAT("Invalid password format", HttpStatus.BAD_REQUEST),
    BULK_ACTION_IDS_REQUIRED("Ids is required", HttpStatus.BAD_REQUEST),
    BULK_ACTION_REQUIRED("action is required", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("You do not have permission", HttpStatus.FORBIDDEN),
    LOGIN_FAILED("Login failed", HttpStatus.BAD_REQUEST),
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

    ACCOUNT_ADMIN_CODE_REQUIRED("Account admin code is required", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADMIN_FULL_NAME_REQUIRED("Account admin full name is required", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADMIN_JOB_TITLE_REQUIRED("Account admin job title is required", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADMIN_EMAIL_REQUIRED("Account admin email is required", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADMIN_PHONE_NUMBER_REQUIRED("Account admin phone number is required", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADMIN_PASSWORD_REQUIRED("Account admin password is required", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADMIN_ROLES_REQUIRED("Account admin roles are required", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADMIN_EXISTED("Account admin already exists", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADMIN_NOT_FOUND("Account admin not found", HttpStatus.NOT_FOUND),

    ACCOUNT_USER_NOT_FOUND("Account user not found", HttpStatus.NOT_FOUND),

    CATEGORY_NAME_REQUIRED("Category name is required", HttpStatus.BAD_REQUEST),
    CATEGORY_SLUG_REQUIRED("Category slug is required", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED("Category already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_HAS_CHILDREN("Category cannot be deleted because it has child categories", HttpStatus.BAD_REQUEST),
    CATEGORY_PARENT_INVALID("Category parent is invalid", HttpStatus.BAD_REQUEST),
    CATEGORY_CIRCULAR_REFERENCE("Category circular reference detected",  HttpStatus.BAD_REQUEST),

    BRAND_NAME_REQUIRED("Brand name is required", HttpStatus.BAD_REQUEST),
    BRAND_SLUG_REQUIRED("Brand slug is required", HttpStatus.BAD_REQUEST),
    BRAND_EXISTED("Brand already exists", HttpStatus.BAD_REQUEST),
    BRAND_NOT_FOUND("Brand not found", HttpStatus.NOT_FOUND),

    COUPON_CODE_REQUIRED("Coupon code is required", HttpStatus.BAD_REQUEST),
    COUPON_NAME_REQUIRED("Coupon name is required", HttpStatus.BAD_REQUEST),
    COUPON_VALUE_REQUIRED("Coupon value is required", HttpStatus.BAD_REQUEST),
    COUPON_START_DATE_REQUIRED("Coupon start date is required", HttpStatus.BAD_REQUEST),
    COUPON_END_DATE_REQUIRED("Coupon end date is required", HttpStatus.BAD_REQUEST),
    COUPON_EXISTED("Coupon already exists", HttpStatus.BAD_REQUEST),
    COUPON_NOT_FOUND("Coupon not found", HttpStatus.NOT_FOUND),
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
