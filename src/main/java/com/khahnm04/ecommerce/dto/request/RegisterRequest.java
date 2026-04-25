package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record RegisterRequest(

    @ValidEmail
    String email,

    @ValidPhoneNumber
    @NotBlank(message = "CUSTOMER_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    String password,
    String confirmPassword,

    @NotBlank(message = "CUSTOMER_FULL_NAME_REQUIRED")
    String fullName,

    LocalDate dateOfBirth

) implements Serializable {
}
