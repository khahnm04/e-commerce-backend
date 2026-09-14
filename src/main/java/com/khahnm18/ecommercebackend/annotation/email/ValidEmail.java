package com.khahnm18.ecommercebackend.annotation.email;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "INVALID_EMAIL_FORMAT";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
