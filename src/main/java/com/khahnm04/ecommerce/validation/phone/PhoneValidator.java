package com.khahnm04.ecommerce.validation.phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String VIETNAM_PATTERN = "^(\\+84|84|0)(3[2-9]|5[2689]|7[06-9]|8[0-9]|9[0-9])[0-9]{7}$";
    private static final String INTERNATIONAL_PATTERN = "^\\+[1-9][0-9]{6,14}$";
    private static final Pattern VN_PATTERN = Pattern.compile(VIETNAM_PATTERN);
    private static final Pattern INTL_PATTERN = Pattern.compile(INTERNATIONAL_PATTERN);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) return true;

        String normalized = phone.replaceAll("[\\s\\-]", "");
        if (normalized.length() > 15) return false;
        if (normalized.matches(".*[a-zA-Z].*")) return false;
        if (normalized.matches(".*(.)\\1{7,}.*")) return false;
        return VN_PATTERN.matcher(normalized).matches() || INTL_PATTERN.matcher(normalized).matches();
    }

}
