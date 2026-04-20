package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.enums.NewsStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record NewsRequest(

    @NotBlank(message = "NEWS_TITLE_REQUIRED")
    String title,

    String slug,

    @NotBlank(message = "NEWS_SUMMARY_REQUIRED")
    String summary,

    @NotBlank(message = "NEWS_CONTENT_REQUIRED")
    String content,

    @NotBlank(message = "NEWS_THUMBNAIL_REQUIRED")
    String thumbnail,

    @NotNull(message = "NEWS_STATUS_REQUIRED")
    NewsStatus status

) implements Serializable {
}
