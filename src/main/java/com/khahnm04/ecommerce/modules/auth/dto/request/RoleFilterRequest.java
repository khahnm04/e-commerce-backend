package com.khahnm04.ecommerce.modules.auth.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleFilterRequest implements Serializable {

    @Builder.Default
    @Min(value = 1, message = "LIST_PAGE_MIN")
    private Integer page = 1;

    @Builder.Default
    @Min(value = 1, message = "LIST_SIZE_MIN")
    @Max(value = 100, message = "LIST_SIZE_MAX")
    private Integer size = 10;

    @Builder.Default
    private String sort = "createdAt,desc";

    private String keyword;

}
