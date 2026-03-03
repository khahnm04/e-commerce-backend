package com.khahnm04.ecommerce.modules.auth.dto.response;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse implements Serializable {

    private Long id;
    private String code;
    private String name;
    private String module;
    private String description;
    private LocalDateTime createdAt;

}
