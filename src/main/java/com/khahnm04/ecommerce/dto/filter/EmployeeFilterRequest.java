package com.khahnm04.ecommerce.dto.filter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record EmployeeFilterRequest(

    @Min(value = 1, message = "LIST_PAGE_MIN")
    Integer page,

    @Min(value = 1, message = "LIST_SIZE_MIN")
    @Max(value = 100, message = "LIST_SIZE_MAX")
    Integer size,

    Boolean isDeleted,
    String sort,
    String keyword,
    String creator,
    Instant startDate,
    Instant endDate

) implements Serializable {
    public EmployeeFilterRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt,desc";
    }
}
