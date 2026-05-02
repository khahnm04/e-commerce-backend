package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.ProductStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductProjection {

    Long getId();
    String getName();
    String getSlug();
    String getCode();
    String getBrandName();
    Long getBrandId();
    Integer getWarrantyMonths();
    BigDecimal getPrice();
    BigDecimal getOldPrice();
    String getThumbnail();
    String getDescription();
    String getShortDescription();
    ProductStatus getStatus();
    Boolean getIsFeatured();
    Boolean getIsTrending();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
    String getCreatedByFullName();
    String getUpdatedByFullName();
    String getDeletedByFullName();

}
