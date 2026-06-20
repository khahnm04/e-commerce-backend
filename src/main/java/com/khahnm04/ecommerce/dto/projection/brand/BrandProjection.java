package com.khahnm04.ecommerce.dto.projection.brand;

import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import java.time.Instant;

public interface BrandProjection {

    Long getId();
    String getName();
    String getSlug();
    String getLogo();
    BrandStatus getStatus();
    Instant getCreatedAt();
    String getCreatedBy();
    Instant getUpdatedAt();
    String getUpdatedBy();
    Instant getDeletedAt();
    String getDeletedBy();
    Boolean getIsDeleted();

}
