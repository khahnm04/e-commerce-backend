package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public interface ProductVariantProjection {

    Long getId();
    String getProductName();
    String getSku();
    String getName();
    BigDecimal getPrice();
    BigDecimal getOldPrice();
    String getImage();
    Map<String, Object> getAttributes();
    ProductVariantStatus getStatus();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
    String getCreatedByFullName();
    String getUpdatedByFullName();
    String getDeletedByFullName();

}
