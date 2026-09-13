package com.khahnm18.ecommercebackend.dto.response;

import org.springframework.data.domain.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(

    int status,
    String message,
    Meta meta,
    List<T> data

) {
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static record Meta(
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean isEmpty
    ) {
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
