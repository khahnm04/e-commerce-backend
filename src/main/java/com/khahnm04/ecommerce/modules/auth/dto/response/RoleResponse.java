package com.khahnm04.ecommerce.modules.auth.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse extends BaseResponse<Long> {

    private String code;
    private String name;
    private String description;
    private Set<String> permissions;

}
