package com.khahnm04.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record BulkActionRequest(

    @NotEmpty(message = "Item IDs are required")
    Set<Long> ids,

    @NotBlank(message = "Action is required")
    String action

) {
}
