package com.khahnm04.ecommerce.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .servers(List.of(new Server().url(openApiProperties.getServerUrl())
                .description(openApiProperties.getServerDescription())))
            .addSecurityItem(new SecurityRequirement()
                .addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication", createBearerAuthScheme()))
            .info(new Info()
                .title(openApiProperties.getTitle())
                .description(openApiProperties.getDescription())
                .version(openApiProperties.getVersion())
                .contact(new Contact()
                    .name(openApiProperties.getContact().getName())
                    .email(openApiProperties.getContact().getEmail())));
    }

    private SecurityScheme createBearerAuthScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Enter JWT token");
    }

}
