package com.khahnm04.ecommerce.dto.request.user;

import com.khahnm04.ecommerce.constant.user.EmployeeStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import com.khahnm04.ecommerce.validation.email.ValidEmail;
import com.khahnm04.ecommerce.validation.password.ValidPassword;
import com.khahnm04.ecommerce.validation.phone.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record EmployeeRequest (

    @NotBlank(message = "EMPLOYEE_CODE_REQUIRED")
    String code,

    @NotBlank(message = "EMPLOYEE_FULL_NAME_REQUIRED")
    String fullName,

    @NotBlank(message = "EMPLOYEE_JOB_TITLE_REQUIRED")
    String jobTitle,

    @ValidEmail
    @NotBlank(message = "EMPLOYEE_EMAIL_REQUIRED")
    String email,

    @ValidPhone
    @NotBlank(message = "EMPLOYEE_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    @ValidPassword
    @NotBlank(message = "EMPLOYEE_PASSWORD_REQUIRED")
    String password,

    Instant dateOfBirth,

    String avatar,

    Gender gender,

    EmployeeStatus status

) implements Serializable {
}
