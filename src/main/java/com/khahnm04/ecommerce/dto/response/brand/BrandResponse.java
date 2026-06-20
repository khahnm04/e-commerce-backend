package com.khahnm04.ecommerce.dto.response.brand;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BrandResponse(

    Long id,
    String name,
    String slug,
    String logo,
    BrandStatus status,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy,
    Instant deletedAt,
    String deletedBy,
    Boolean isDeleted

) implements Serializable {
}
