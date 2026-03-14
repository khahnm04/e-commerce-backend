package com.khahnm04.ecommerce.modules.user.dto.request;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Builder
public record EmployeeCreationRequest(

    @ValidEmail
    String email,

    @ValidPhoneNumber
    @NotBlank(message = "EMPLOYEE_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    String password,
    String confirmPassword,

    @NotBlank(message = "EMPLOYEE_CODE_REQUIRED")
    String code,

    @NotBlank(message = "EMPLOYEE_FULL_NAME_REQUIRED")
    String fullName,

    String jobTitle,
    LocalDate dateOfBirth,
    String avatar,
    Gender gender,
    UserStatus status,

    @NotEmpty(message = "EMPLOYEE_ROLES_REQUIRED")
    Set<Long> roles

) implements Serializable {
}
