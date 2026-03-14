package com.khahnm04.ecommerce.common.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.domain.Page;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(

        List<T> data,
        ApiResponse.PageMetadata meta

) {

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return PageResponse.<T>builder()
                .data(page.getContent())
                .meta(ApiResponse.PageMetadata.builder()
                        .pageNo(page.getNumber() + 1)
                        .pageSize(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .isLast(page.isLast())
                        .isFirst(page.isFirst())
                        .isEmpty(page.isEmpty())
                        .build())
                .build();
    }

}
