package com.khahnm18.ecommercebackend.annotation.ratelimit;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    String keyPrefix();
    int capacity() default 5;
    int refillSeconds() default 60;
}
