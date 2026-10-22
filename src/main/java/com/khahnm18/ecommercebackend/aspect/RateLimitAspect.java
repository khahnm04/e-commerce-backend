package com.khahnm18.ecommercebackend.aspect;

import com.khahnm18.ecommercebackend.service.ratelimit.RateLimitService;
import com.khahnm18.ecommercebackend.annotation.ratelimit.RateLimit;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RateLimitService rateLimitService;

    @Around("@annotation(rateLimit)")
    public Object enforceRateLimit(
        ProceedingJoinPoint joinPoint,
        RateLimit rateLimit
    ) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            rateLimitService.checkRateLimit(
                attributes.getRequest(),
                rateLimit.keyPrefix(),
                rateLimit.capacity(),
                rateLimit.refillSeconds()
            );
        }
        return joinPoint.proceed();
    }

}
