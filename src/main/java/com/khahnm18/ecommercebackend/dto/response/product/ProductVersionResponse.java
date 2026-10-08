package com.khahnm18.ecommercebackend.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm18.ecommercebackend.entity.product.SpecificationGroup;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductVersionResponse(

    UUID id,
    UUID groupId,
    String name,
    String slug,
    String version,
    BigDecimal ratingAvg,
    Integer ratingCount,
    BigDecimal priceOld,
    BigDecimal priceNew,
    Integer totalStock,
    Integer soldCount,
    List<SpecificationGroup> specifications,
    String description,
    String content,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
