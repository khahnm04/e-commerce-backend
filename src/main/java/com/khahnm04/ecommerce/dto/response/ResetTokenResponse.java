package com.khahnm04.ecommerce.dto.response;

import lombok.Builder;

@Builder
public record ResetTokenResponse(
    String resetToken
) {
}
