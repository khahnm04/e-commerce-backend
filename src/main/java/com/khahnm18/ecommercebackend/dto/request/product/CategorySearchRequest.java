package com.khahnm18.ecommercebackend.dto.request.product;

import com.khahnm18.ecommercebackend.enums.CategoryStatus;
import java.time.Instant;
import java.util.UUID;

public record CategorySearchRequest(

    Boolean isDeleted,
    String keyword,
    Instant startDate,
    Instant endDate,
    CategoryStatus status,
    UUID parentId

) {}

