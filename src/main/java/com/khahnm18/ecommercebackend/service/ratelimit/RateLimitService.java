package com.khahnm18.ecommercebackend.service.ratelimit;

import com.khahnm18.ecommercebackend.exception.RateLimitException;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;
import java.time.Duration;

@Slf4j(topic = "RATE-LIMIT-SERVICE")
@Component
@RequiredArgsConstructor
public class RateLimitService {

    private final ProxyManager<String> proxyManager;

    public void checkRateLimit(
        HttpServletRequest request,
        String keyPrefix,
        int capacity,
        int refillSeconds
    ) {
        String clientKey = getClientIdentifier(request);
        String redisKey = String.format("rate_limit:%s:%s", keyPrefix, clientKey);
        try {
            Bucket bucket = proxyManager.builder().build(redisKey, () -> createConfig(capacity, refillSeconds));
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
            if (!probe.isConsumed()) {
                long waitSeconds = Math.max(1, TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));
                throw new RateLimitException(waitSeconds);
            }
        } catch (RateLimitException ex) {
            throw ex;
        } catch (Exception ex) {
            log.warn("Rate Limit Error: {}", ex.getMessage());
        }
    }

    private String getClientIdentifier(HttpServletRequest request) {
        if (SecurityUtil.isAuthenticated()) {
            return "user:" + SecurityContextHolder.getContext().getAuthentication().getName();
        }
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return "ip:" + forwardedFor.split(",")[0].trim();
        }
        return "ip:" + request.getRemoteAddr();
    }

    private BucketConfiguration createConfig(int capacity, int refillSeconds) {
        Bandwidth limit = Bandwidth.builder()
            .capacity(capacity)
            .refillGreedy(capacity, Duration.ofSeconds(refillSeconds))
            .build();
        return BucketConfiguration.builder().addLimit(limit).build();
    }

}
