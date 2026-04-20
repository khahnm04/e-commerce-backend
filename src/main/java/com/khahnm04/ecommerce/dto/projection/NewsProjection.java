package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.NewsStatus;
import java.time.LocalDateTime;

public interface NewsProjection {

    Long getId();
    String getTitle();
    String getSlug();
    String getSummary();
    String getContent();
    String getThumbnail();
    NewsStatus getStatus();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
    String getCreatedByFullName();
    String getUpdatedByFullName();
    String getDeletedByFullName();

}
