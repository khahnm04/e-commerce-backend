package com.khahnm04.ecommerce.dto.filter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record RoleFilterRequest(

    @Min(value = 1, message = "LIST_PAGE_MIN")
    Integer page,

    @Min(value = 1, message = "LIST_SIZE_MIN")
    @Max(value = 100, message = "LIST_SIZE_MAX")
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
