package com.khahnm04.ecommerce.modules.auth.dto.request;

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
public class RoleBulkActionRequest implements Serializable {

    @NotEmpty(message = "ROLE_SELECTION_REQUIRED")
    private Set<Long> ids;

    @NotBlank(message = "ROLE_ACTION_REQUIRED")
    private String action;

}
