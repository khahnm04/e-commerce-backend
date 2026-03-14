package com.khahnm04.ecommerce.modules.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AttributeResponse(

    Long id,
    String name,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt

) implements Serializable {
}
