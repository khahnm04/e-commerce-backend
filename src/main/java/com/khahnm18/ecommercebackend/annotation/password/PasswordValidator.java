package com.khahnm18.ecommercebackend.annotation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private int minLength;
    private int maxLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialChar;

    @Override
    public void initialize(ValidPassword annotation) {
        this.minLength = annotation.minLength();
        this.maxLength = annotation.maxLength();
        this.requireUppercase = annotation.requireUppercase();
        this.requireLowercase = annotation.requireLowercase();
        this.requireDigit = annotation.requireDigit();
        this.requireSpecialChar = annotation.requireSpecialChar();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) return true;

        if (password.length() < minLength || password.length() > maxLength) return false;
        if (requireUppercase && !password.matches(".*[A-Z].*")) return false;
        if (requireLowercase && !password.matches(".*[a-z].*")) return false;
        if (requireDigit && !password.matches(".*[0-9].*")) return false;
        if (requireSpecialChar && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) return false;
        return !password.contains(" ");
    }

}
