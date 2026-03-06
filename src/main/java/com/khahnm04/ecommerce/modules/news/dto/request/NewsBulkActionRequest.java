package com.khahnm04.ecommerce.modules.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsBulkActionRequest implements Serializable {

    @NotEmpty(message = "NEWS_SELECTION_REQUIRED")
    private Set<Long> news;

    @NotBlank(message = "NEWS_ACTION_REQUIRED")
    private String action;

}
