package com.khahnm04.ecommerce.dto.response;

import lombok.Builder;

@Builder
public record CloudinarySignatureResponse(

        String signature,
        long timestamp,
        String cloudName,
        String apiKey,
        String folder

) {
}
