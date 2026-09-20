package com.khahnm18.ecommercebackend.dto.response.cloudinary;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SignatureResponse(

    String signature,
    long timestamp,
    String cloudName,
    String apiKey,
    String folder

) {}
