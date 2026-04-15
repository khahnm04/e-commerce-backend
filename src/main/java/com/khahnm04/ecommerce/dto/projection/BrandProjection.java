package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.BrandStatus;
import java.time.LocalDateTime;

public interface BrandProjection {

    Long getId();
    String getName();
    String getSlug();
    String getDescription();
    String getLogo();
    BrandStatus getStatus();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
    String getCreatedByFullName();
    String getUpdatedByFullName();
    String getDeletedByFullName();

}
