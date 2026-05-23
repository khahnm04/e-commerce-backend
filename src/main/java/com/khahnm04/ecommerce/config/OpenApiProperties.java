package com.khahnm04.ecommerce.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "openapi.service")
public class OpenApiProperties {

    private String serverUrl;
    private String serverDescription;
    private String title;
    private String description;
    private String version;
    private Contact contact;

    @Getter
    @Setter
    public static class Contact {
        private String name;
        private String email;
    }

}
