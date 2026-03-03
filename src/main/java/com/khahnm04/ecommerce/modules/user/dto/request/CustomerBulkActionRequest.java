package com.khahnm04.ecommerce.modules.user.dto.request;

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
public class CustomerBulkActionRequest implements Serializable {

    @NotEmpty(message = "CUSTOMER_SELECTION_REQUIRED")
    private Set<Long> customers;

    @NotBlank(message = "CUSTOMER_ACTION_REQUIRED")
    private String action;

}
