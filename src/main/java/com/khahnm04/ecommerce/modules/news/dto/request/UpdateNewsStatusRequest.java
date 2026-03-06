package com.khahnm04.ecommerce.modules.news.dto.request;

import com.khahnm04.ecommerce.modules.news.enums.NewsStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNewsStatusRequest {

    @NotNull(message = "NEWS_STATUS_REQUIRED")
    private NewsStatus status;

}
