package com.khahnm04.ecommerce.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.io.Serializable;
import java.util.Set;

@Builder
public record RoleRequest(

    @NotBlank(message = "ROLE_CODE_REQUIRED")
    String code,

    @NotBlank(message = "ROLE_NAME_REQUIRED")
    String name,

    @NotEmpty(message = "ROLE_PERMISSIONS_REQUIRED")
    Set<Long> permissions

) implements Serializable {
}

