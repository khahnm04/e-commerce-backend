package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.domain.Page;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(

    Instant timestamp,
    Boolean success,
    String message,
    Meta meta,
    List<T> data

) implements Serializable {
    public PageResponse {
        if (timestamp == null) timestamp = Instant.now();
        if (success == null) success = true;
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static record Meta(

        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean isEmpty

    ) implements Serializable {
        public static Meta fromPage(Page<?> page) {
            return Meta.builder()
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .isEmpty(page.isEmpty())
                .build();
        }
    }
}
