package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.domain.Page;
import java.io.Serializable;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(

    List<T> data,
    PageMetaResponse meta

) implements Serializable {

    public static <T> PageResponse<T> fromPage(Page<T> page) {
    return PageResponse.<T>builder()
        .data(page.getContent())
        .meta(PageMetaResponse.builder()
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
