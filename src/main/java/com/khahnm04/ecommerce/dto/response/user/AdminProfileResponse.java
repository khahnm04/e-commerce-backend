package com.khahnm04.ecommerce.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.constant.user.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminProfileResponse(

    String fullName,
    String jobTitle,
    String email,
    String phoneNumber,
    Instant dateOfBirth,
    String avatar,
    Gender gender

) implements Serializable {
}
