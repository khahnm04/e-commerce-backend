package com.khahnm04.ecommerce.modules.auth.dto.response;

import lombok.Builder;
import java.io.Serializable;

@Builder
public record LoginResponse(

    Long id,
    String fullName,
    String avatar

) implements Serializable {
}
