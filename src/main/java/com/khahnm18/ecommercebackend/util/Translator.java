package com.khahnm18.ecommercebackend.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Translator {

    private final MessageSource messageSource;

    public String toLocale(String msgCode) {
        return toLocale(msgCode, msgCode);
    }

    public String toLocale(String msgCode, String defaultMessage) {
        return messageSource.getMessage(
            msgCode,
            null,
            defaultMessage,
            LocaleContextHolder.getLocale()
        );
    }

}
