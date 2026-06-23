package com.khahnm04.ecommerce.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import static com.khahnm04.ecommerce.constant.TokenConstant.ACCESS_TOKEN;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    @Value("${openapi.service.server-url}")
    private String serverUrl;

    @Value("${openapi.service.server-description}")
    private String serverDescription;

    @Value("${openapi.service.title}")
    private String title;

    @Value("${openapi.service.description}")
    private String description;

    @Value("${openapi.service.version}")
    private String version;

    @Value("${openapi.service.contact.name}")
    private String contactName;

    @Value("${openapi.service.contact.email}")
    private String contactEmail;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .servers(List.of(new Server().url(serverUrl)
                .description(serverDescription)))
            .addSecurityItem(new SecurityRequirement()
                .addList("Bearer Authentication")
                .addList("cookieAuth"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication", createBearerAuthScheme())
                .addSecuritySchemes("cookieAuth", createCookieAuthScheme()))
            .info(new Info()
                .title(title)
                .description(description)
                .version(version)
                .contact(new Contact()
                    .name(contactName)
                    .email(contactEmail)));
    }

    private SecurityScheme createBearerAuthScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Enter JWT token");
    }

    private SecurityScheme createCookieAuthScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .in(SecurityScheme.In.COOKIE)
            .name(ACCESS_TOKEN)
            .description("Enter access token cookie value");
    }

}
