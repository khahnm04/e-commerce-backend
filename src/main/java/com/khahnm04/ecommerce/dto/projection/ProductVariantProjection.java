package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductVariantProjection {

    Long getId();
    String getProductName();
    String getSku();
    String getName();
    BigDecimal getPrice();
    BigDecimal getOldPrice();
    String getImage();
    ProductVariantStatus getStatus();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
    String getCreatedByFullName();
    String getUpdatedByFullName();
    String getDeletedByFullName();

}
