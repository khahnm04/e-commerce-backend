package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.enums.Gender;
import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record EmployeeProfileUpdateRequest(

    @NotBlank(message = "EMPLOYEE_FULL_NAME_REQUIRED")
    String fullName,

    @ValidEmail
    String email,

    @ValidPhoneNumber
    @NotBlank(message = "EMPLOYEE_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    LocalDate dateOfBirth,
    Gender gender,
    String avatar

) implements Serializable {
}
