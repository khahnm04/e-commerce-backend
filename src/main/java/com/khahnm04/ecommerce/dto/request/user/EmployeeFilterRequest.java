package com.khahnm04.ecommerce.dto.request.user;

import com.khahnm04.ecommerce.common.enums.UserStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record EmployeeFilterRequest(

    @Min(value = 1, message = "LIST_PAGE_MIN")
    Integer page,

    @Min(value = 1, message = "LIST_SIZE_MIN")
    @Max(value = 100, message = "LIST_SIZE_MAX")
    Integer size,

    String sort,
    UserStatus status,
    String keyword,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate

) implements Serializable {

    public EmployeeFilterRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt,desc";
    }

}
