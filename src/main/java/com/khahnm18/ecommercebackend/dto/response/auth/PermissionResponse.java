package com.khahnm18.ecommercebackend.dto.response.auth;

import lombok.Builder;
import java.util.UUID;

@Builder
public record PermissionResponse(

    UUID id,
    String code,
    String name

) {}
