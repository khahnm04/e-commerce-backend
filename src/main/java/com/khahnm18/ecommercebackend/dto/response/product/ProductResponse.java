package com.khahnm18.ecommercebackend.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm18.ecommercebackend.enums.ProductStatus;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponse(

    UUID id,
    UUID versionId,
    String name,
    String slug,
    String color,
    String sku,
    Boolean isDefault,
    BigDecimal priceOld,
    BigDecimal priceNew,
    Integer discount,
    Integer stock,
    String avatar,
    ProductStatus status,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
