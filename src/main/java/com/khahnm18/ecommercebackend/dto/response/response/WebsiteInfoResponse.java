package com.khahnm18.ecommercebackend.dto.response.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WebsiteInfoResponse(

    UUID id,
    String name,
    String phoneNumber,
    String email,
    String address,
    String logo,
    String favicon,
    Instant createdAt

) {}
