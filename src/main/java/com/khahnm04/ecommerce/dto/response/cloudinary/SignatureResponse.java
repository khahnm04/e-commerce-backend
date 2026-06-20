package com.khahnm04.ecommerce.dto.response.cloudinary;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SignatureResponse(

    String signature,
    long timestamp,
    String cloudName,
    String apiKey,
    String folder

) implements Serializable {
}
