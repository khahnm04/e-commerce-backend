package com.khahnm04.ecommerce.dto.projection.category;

import com.khahnm04.ecommerce.constant.category.CategoryStatus;
import java.time.Instant;

public interface CategoryProjection {

    Long getId();
    Long getParentId();
    String getName();
    String getSlug();
    String getThumbnail();
    Boolean getIsFeatured();
    Boolean getShowInMenu();
    CategoryStatus getStatus();
    Instant getCreatedAt();
    String getCreatedBy();
    Instant getUpdatedAt();
    String getUpdatedBy();
    Instant getDeletedAt();
    String getDeletedBy();
    Boolean getIsDeleted();

}
