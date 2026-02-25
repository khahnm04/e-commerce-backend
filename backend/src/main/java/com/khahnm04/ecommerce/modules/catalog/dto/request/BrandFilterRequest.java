package com.khahnm04.ecommerce.modules.catalog.dto.request;

import com.khahnm04.ecommerce.modules.catalog.enums.BrandStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandFilterRequest implements Serializable {

    @Builder.Default
    @Min(value = 1, message = "LIST_PAGE_MIN")
    private Integer page = 1;

    @Builder.Default
    @Min(value = 1, message = "LIST_SIZE_MIN")
    @Max(value = 100, message = "LIST_SIZE_MAX")
    private Integer size = 10;

    @Builder.Default
    private String sort = "createdAt,desc";

    private BrandStatus status;

    private String keyword;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
