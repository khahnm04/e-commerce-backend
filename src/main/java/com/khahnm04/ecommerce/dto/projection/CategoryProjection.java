package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import java.time.LocalDateTime;

public interface CategoryProjection {

    Long getId();
    String getName();
    String getSlug();
    String getDescription();
    String getThumbnail();
    String getIcon();
    Boolean getIsFeatured();
    Integer getPosition();
    CategoryStatus getStatus();
    Long getParentId();
    String getParentName();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
    String getCreatedByFullName();
    String getUpdatedByFullName();
    String getDeletedByFullName();

}
