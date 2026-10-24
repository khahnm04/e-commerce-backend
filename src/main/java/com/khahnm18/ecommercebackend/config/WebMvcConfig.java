package com.khahnm18.ecommercebackend.config;

import com.khahnm18.ecommercebackend.config.converter.InstantConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.jspecify.annotations.NonNull;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final InstantConverter instantConverter;

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        ApplicationConversionService.configure(registry);
        registry.addConverter(instantConverter);
    }

}
