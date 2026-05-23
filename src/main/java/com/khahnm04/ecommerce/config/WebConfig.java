package com.khahnm04.ecommerce.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final InstantConverter instantConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(instantConverter);
    }

}
