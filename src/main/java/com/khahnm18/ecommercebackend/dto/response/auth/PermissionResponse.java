package com.khahnm18.ecommercebackend.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionResponse(

    UUID id,
    String code,
    String name

) {}
