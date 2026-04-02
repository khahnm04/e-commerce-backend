package com.khahnm04.ecommerce.dto.response.auth;

import lombok.Builder;
import java.io.Serializable;

@Builder
public record LoginResponse(

    Long id,
    String fullName,
    String avatar

) implements Serializable {
}
