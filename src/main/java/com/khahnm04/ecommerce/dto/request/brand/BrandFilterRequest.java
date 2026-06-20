package com.khahnm04.ecommerce.dto.request.brand;

import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record BrandFilterRequest(

    Integer page,
    Integer size,
    Boolean isDeleted,
    String sort,
    String keyword,
    Instant startDate,
    Instant endDate,
    BrandStatus status

) implements Serializable {
    public BrandFilterRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt,desc";
        if (isDeleted == null) isDeleted = false;
    }
}
