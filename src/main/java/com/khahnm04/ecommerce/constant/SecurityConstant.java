package com.khahnm04.ecommerce.constant;

public class SecurityConstant {

    public static final String[] PUBLIC_GET_ENDPOINTS = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-ui.html",
    };

    public static final String[] ADMIN_PUBLIC_POST_ENDPOINTS = {
        "/api/v1/auth/admin/**",
    };

    public static final String[] CLIENT_PUBLIC_POST_ENDPOINTS = {
        "/api/v1/auth/**",
    };

    private SecurityConstant() {
    }

}
