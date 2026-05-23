package com.khahnm04.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.io.Serializable;
import java.util.Set;

@Builder
public record BulkActionRequest(

    @NotEmpty(message = "BULK_ACTION_IDS_REQUIRED")
    Set<Long> ids,

    @NotBlank(message = "BULK_ACTION_REQUIRED")
    String action

) implements Serializable {
}
