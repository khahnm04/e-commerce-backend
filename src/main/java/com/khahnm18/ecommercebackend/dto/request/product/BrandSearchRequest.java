package com.khahnm18.ecommercebackend.dto.request.product;

import com.khahnm18.ecommercebackend.enums.BrandStatus;
import java.time.Instant;

public record BrandSearchRequest(

    Boolean isDeleted,
    String keyword,
    Instant startDate,
    Instant endDate,
    BrandStatus status

) {}

