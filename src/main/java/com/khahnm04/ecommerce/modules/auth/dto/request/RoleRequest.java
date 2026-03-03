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
public class RoleRequest implements Serializable {

    @NotBlank(message = "ROLE_CODE_REQUIRED")
    private String code;

    @NotBlank(message = "ROLE_NAME_REQUIRED")
    private String name;

    @NotBlank(message = "ROLE_DESCRIPTION_REQUIRED")
    private String description;

    @NotEmpty(message = "ROLE_PERMISSIONS_REQUIRED")
    private Set<Long> permissions;

}
