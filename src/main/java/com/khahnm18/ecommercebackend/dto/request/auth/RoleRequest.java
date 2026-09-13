package com.khahnm18.ecommercebackend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record RoleRequest(

    @NotBlank(message = "role.code.required")
    String code,

    @NotBlank(message = "role.name.required")
    String name,

    @NotEmpty(message = "role.permissions.required")
    List<UUID> permissions

) {}
