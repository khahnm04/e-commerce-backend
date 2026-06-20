package com.khahnm04.ecommerce.dto.request.user;

import com.khahnm04.ecommerce.constant.user.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
public record AdminProfileRequest(

    @NotBlank(message = "ACCOUNT_ADMIN_FULL_NAME_REQUIRED")
    String fullName,

    String jobTitle,

    @NotBlank(message = "ACCOUNT_ADMIN_EMAIL_REQUIRED")
    String email,

    @NotBlank(message = "ACCOUNT_ADMIN_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    Instant dateOfBirth,
    String avatar,
    Gender gender

) implements Serializable {
}
