package com.khahnm04.ecommerce.dto.projection.auth;

import java.time.LocalDateTime;

public interface RoleProjection {
    Long getId();

    String getCode();

    String getName();

    String getDescription();

    LocalDateTime getCreatedAt();

    String getCreatedByFullName();

    LocalDateTime getUpdatedAt();

    String getUpdatedByFullName();

    LocalDateTime getDeletedAt();
}
