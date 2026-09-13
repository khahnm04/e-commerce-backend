package com.khahnm18.ecommercebackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.util.Set;
import java.util.UUID;

@Builder
public record BulkActionRequest(

    @NotEmpty(message = "bulk.ids.required")
    Set<UUID> ids,

    @NotBlank(message = "bulk.action.required")
    String action

) {}
