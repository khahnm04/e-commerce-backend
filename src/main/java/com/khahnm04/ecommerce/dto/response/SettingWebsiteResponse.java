package com.khahnm04.ecommerce.dto.response;

import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record SettingWebsiteResponse(
  
    Long id,
    String websiteName,
    String phoneNumber,
    String email,
    String address,
    String logo,
    String favicon,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) implements Serializable {
}
