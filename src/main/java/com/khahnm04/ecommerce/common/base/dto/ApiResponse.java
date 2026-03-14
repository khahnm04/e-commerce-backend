package com.khahnm04.ecommerce.common.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(

        LocalDateTime timestamp,
        Boolean success,
        String message,
        PageMetadata meta,
        T data

) implements Serializable {

    public ApiResponse {
        if (timestamp == null)
            timestamp = LocalDateTime.now();
        if (success == null)
            success = true;
    }

    @Builder
    public static record PageMetadata(

            int pageNo,
            int pageSize,
            long totalElements,
            int totalPages,
            boolean isFirst,
            boolean isLast,
            boolean isEmpty

    ) implements Serializable {
    }

}
