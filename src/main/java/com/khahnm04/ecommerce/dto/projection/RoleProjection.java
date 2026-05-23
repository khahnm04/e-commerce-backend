package com.khahnm04.ecommerce.dto.projection;

import java.time.Instant;

public interface RoleProjection {

    Long getId();
    String getCode();
    String getName();
    String getDescription();
    Instant getCreatedAt();
    String getCreatedBy();
    Instant getUpdatedAt();
    String getUpdatedBy();
    Instant getDeletedAt();
    String getDeletedBy();

}
