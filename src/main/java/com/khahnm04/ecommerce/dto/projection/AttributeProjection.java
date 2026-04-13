package com.khahnm04.ecommerce.dto.projection;

import java.time.LocalDateTime;

public interface AttributeProjection {
  Long getId();

  String getName();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

  LocalDateTime getDeletedAt();

  String getCreatedByFullName();

  String getUpdatedByFullName();
}
