package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.BannerPosition;
import com.khahnm04.ecommerce.common.enums.BannerStatus;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BannerResponse(

    Long id,
    String image,
    BannerPosition position,
    Integer sortOrder,
    String label,
    String heading,
    String description,
    String ctaText,
    String ctaUrl,
    LocalDateTime countdownEnd,
    BannerStatus status,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy

) implements Serializable {
}
