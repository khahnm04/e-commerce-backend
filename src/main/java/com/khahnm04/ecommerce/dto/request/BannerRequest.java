package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.enums.BannerPosition;
import com.khahnm04.ecommerce.common.enums.BannerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record BannerRequest(

    @NotBlank(message = "BANNER_IMAGE_REQUIRED")
    String image,

    @NotNull(message = "BANNER_POSITION_REQUIRED")
    BannerPosition position,

    Integer sortOrder,
    String label,
    String heading,
    String description,
    String ctaText,
    String ctaUrl,
    LocalDateTime countdownEnd,
    BannerStatus status

) implements Serializable {
}
