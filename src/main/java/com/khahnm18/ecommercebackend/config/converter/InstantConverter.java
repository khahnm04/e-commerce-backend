package com.khahnm18.ecommercebackend.config.converter;

import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

@Component
public class InstantConverter implements Converter<String, Instant> {

    @Override
    public Instant convert(@NonNull String source) {
        if (source.isBlank()) return null;
        try {
            return Instant.parse(source);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(source)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC);
            } catch (DateTimeParseException ex) {
                throw new AppException(ErrorCode.INVALID_INSTANT_FORMAT);
            }
        }
    }

}
