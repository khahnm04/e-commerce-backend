package com.khahnm18.ecommercebackend.dto.request.product;

import com.khahnm18.ecommercebackend.enums.ProductStatus;
import java.time.Instant;
import java.util.UUID;

public record ProductSearchRequest(

    Boolean isDeleted,
    String keyword,
    Instant startDate,
    Instant endDate,
    ProductStatus status,
    UUID versionId

) {}

