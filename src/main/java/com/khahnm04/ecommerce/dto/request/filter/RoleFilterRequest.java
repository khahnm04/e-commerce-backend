package com.khahnm04.ecommerce.dto.request.filter;

import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record RoleFilterRequest(

    Integer page,
    Integer size,
    Boolean isDeleted,
    String sort,
    String keyword,
    Instant startDate,
    Instant endDate

) implements Serializable {
    public RoleFilterRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt,desc";
        if (isDeleted == null) isDeleted = false;
    }
}
