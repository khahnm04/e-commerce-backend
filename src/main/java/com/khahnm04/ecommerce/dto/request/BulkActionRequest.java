package com.khahnm04.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.io.Serializable;
import java.util.Set;

@Builder
public record BulkActionRequest(

    @NotEmpty(message = "Item IDs are required")
    Set<Long> ids,

    @NotBlank(message = "Action is required")
    String action

) implements Serializable {
}
