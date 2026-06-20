package com.khahnm04.ecommerce.dto.projection.auth;

import java.time.Instant;

public interface RoleProjection {

    Long getId();
    String getCode();
    String getName();
    Instant getCreatedAt();
    String getCreatedBy();
    Instant getUpdatedAt();
    String getUpdatedBy();
    Instant getDeletedAt();
    String getDeletedBy();
    Boolean getIsDeleted();

}
