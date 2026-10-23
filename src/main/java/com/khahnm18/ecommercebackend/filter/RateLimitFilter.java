package com.khahnm18.ecommercebackend.filter;

import com.khahnm18.ecommercebackend.exception.RateLimitException;
import com.khahnm18.ecommercebackend.service.ratelimit.RateLimitService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.jspecify.annotations.NonNull;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private static final int GUEST_CAPACITY = 30;
    private static final int USER_CAPACITY = 100;
    private static final int REFILL_SECONDS = 60;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
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
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

}
