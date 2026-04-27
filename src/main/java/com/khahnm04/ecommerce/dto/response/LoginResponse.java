package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponse(

    Long id,
    String email,
    String phoneNumber,
    String fullName,
    String avatar,
    String jobTitle,
    String userType,
    String token,
    Boolean isEmailVerified

) implements Serializable {
}
