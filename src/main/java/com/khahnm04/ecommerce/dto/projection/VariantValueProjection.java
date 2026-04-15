package com.khahnm04.ecommerce.dto.projection;

import java.time.LocalDateTime;

public interface VariantValueProjection {

    Long getId();

    String getVariantName();

    Long getVariantId();

    String getValue();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getDeletedAt();

    String getCreatedByFullName();

    String getUpdatedByFullName();

    String getDeletedByFullName();

}
