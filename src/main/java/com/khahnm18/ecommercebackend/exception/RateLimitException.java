package com.khahnm18.ecommercebackend.exception;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {

    private final long retryAfterSeconds;

    public RateLimitException(long retryAfterSeconds) {
        super(ErrorCode.TOO_MANY_REQUESTS.getMessage());
        this.retryAfterSeconds = retryAfterSeconds;
    }

}
