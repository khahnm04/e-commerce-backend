package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import com.khahnm04.ecommerce.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.time.LocalDate;
import java.util.Set;
import java.io.Serializable;

@Builder
public record UserUpdateRequest(

    @ValidEmail
    String email,

    @ValidPhoneNumber
    @NotBlank(message = "Phone number cannot be blank")
    String phoneNumber,

    @NotBlank(message = "Full name cannot be blank")
    String fullName,

    LocalDate dateOfBirth,

    @NotBlank(message = "avatar cannot be blank")
    String avatar,

    Gender gender,

    UserStatus status,

    @NotEmpty(message = "roles cannot be empty")
    Set<Long> roles

) implements Serializable {
}
