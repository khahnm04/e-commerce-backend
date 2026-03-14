package com.khahnm04.ecommerce.modules.auth.dto.request;

import lombok.Builder;
import java.io.Serializable;

@Builder
public record LogoutRequest(

    String refreshToken

) implements Serializable {
}
