package com.khahnm04.ecommerce.modules.user.dto.request;

import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFilterRequest implements Serializable {

    @Builder.Default
    @Min(value = 1, message = "LIST_PAGE_MIN")
    private Integer page = 1;

    @Builder.Default
    @Min(value = 1, message = "LIST_SIZE_MIN")
    @Max(value = 100, message = "LIST_SIZE_MAX")
    private Integer size = 10;

    @Builder.Default
    private String sort = "createdAt,desc";

    private UserStatus status;

    private Gender gender;

    private String keyword;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
