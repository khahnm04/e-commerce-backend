package com.khahnm04.ecommerce.dto.request.user;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import com.khahnm04.ecommerce.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record CustomerUpdateRequest(

    @ValidEmail
    String email,

    @ValidPhoneNumber
    @NotBlank(message = "CUSTOMER_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    @NotBlank(message = "CUSTOMER_FULL_NAME_REQUIRED")
    String fullName,

    LocalDate dateOfBirth,
    String avatar,
    Gender gender,
    UserStatus status

) implements Serializable {
}
