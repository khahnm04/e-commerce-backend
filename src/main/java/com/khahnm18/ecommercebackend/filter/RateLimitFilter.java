package com.khahnm18.ecommercebackend.filter;

import com.khahnm18.ecommercebackend.exception.RateLimitException;
import com.khahnm18.ecommercebackend.service.ratelimit.RateLimitService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.jspecify.annotations.NonNull;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;
import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int GUEST_CAPACITY = 30;
    private static final int USER_CAPACITY = 100;
    private static final int REFILL_SECONDS = 60;

    private final RateLimitService rateLimitService;
    private final HandlerExceptionResolver resolver;

    public RateLimitFilter(
        RateLimitService rateLimitService,
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
    ) {
        this.rateLimitService = rateLimitService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (!request.getServletPath().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            int capacity = SecurityUtil.isAuthenticated() ? USER_CAPACITY : GUEST_CAPACITY;
            rateLimitService.checkRateLimit(request, "global", capacity, REFILL_SECONDS);
            filterChain.doFilter(request, response);
        } catch (RateLimitException ex) {
            resolver.resolveException(request, response, null, ex);
        }
    }

}
