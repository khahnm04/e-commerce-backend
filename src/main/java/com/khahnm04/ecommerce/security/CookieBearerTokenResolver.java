package com.khahnm04.ecommerce.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.WebUtils;
import java.util.Arrays;
import static com.khahnm04.ecommerce.constant.SecurityConstant.*;

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public String resolve(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            boolean isPublicGet = Arrays.stream(PUBLIC_GET_ENDPOINTS)
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));
            if (isPublicGet) {
                return null;
            }
        }

        if ("POST".equalsIgnoreCase(method)) {
            boolean isAdminPublicPost = Arrays.stream(ADMIN_PUBLIC_POST_ENDPOINTS)
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));

            boolean isClientPublicPost = Arrays.stream(CLIENT_PUBLIC_POST_ENDPOINTS)
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));

            if (isAdminPublicPost || isClientPublicPost) {
                return null;
            }
        }

        Cookie tokenCookie = WebUtils.getCookie(request, "access_token");
        return tokenCookie != null ? tokenCookie.getValue() : null;
    }

}
