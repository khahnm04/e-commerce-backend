package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.NewsStatus;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NewsResponse(

    Long id,
    String title,
    String slug,
    String summary,
    String content,
    String thumbnail,
    NewsStatus status,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy

) implements Serializable {
}
