package com.khahnm18.ecommercebackend.dto.response.storage;

import lombok.Builder;

@Builder
public record PresignedResponse(

    String url,
    String key

) {}
