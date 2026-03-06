package com.khahnm04.ecommerce.modules.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {

    @NotBlank(message = "NEWS_TITLE_REQUIRED")
    private String title;

    @NotBlank(message = "NEWS_SLUG_REQUIRED")
    private String slug;

    private String image;

    private String summary;

    @NotBlank(message = "NEWS_CONTENT_REQUIRED")
    private String content;

    @Builder.Default
    private Boolean isFeatured = false;

    private Set<Long> productIds;

}
