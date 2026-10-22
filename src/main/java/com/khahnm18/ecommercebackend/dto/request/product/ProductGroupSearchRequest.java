package com.khahnm18.ecommercebackend.dto.request.product;

import java.time.Instant;
import java.util.UUID;

public record ProductGroupSearchRequest(

    Boolean isDeleted,
    String keyword,
    Instant startDate,
    Instant endDate,
    UUID categoryId

) {}

