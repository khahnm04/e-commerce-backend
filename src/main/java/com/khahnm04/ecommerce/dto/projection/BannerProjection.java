package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.BannerPosition;
import com.khahnm04.ecommerce.common.enums.BannerStatus;
import java.time.LocalDateTime;

public interface BannerProjection {

    Long getId();
    String getImage();
    BannerPosition getPosition();
    Integer getSortOrder();
    String getLabel();
    String getHeading();
    String getDescription();
    String getCtaText();
    String getCtaUrl();
    LocalDateTime getCountdownEnd();
    BannerStatus getStatus();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
    String getCreatedByFullName();
    String getUpdatedByFullName();
    String getDeletedByFullName();

}
