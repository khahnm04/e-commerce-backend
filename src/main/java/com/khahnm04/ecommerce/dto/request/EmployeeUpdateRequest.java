package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import com.khahnm04.ecommerce.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Builder
public record EmployeeUpdateRequest(

    @ValidEmail
    String email,

    @ValidPhoneNumber
    @NotBlank(message = "EMPLOYEE_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    @NotBlank(message = "EMPLOYEE_CODE_REQUIRED")
    String code,

    @NotBlank(message = "EMPLOYEE_FULL_NAME_REQUIRED")
    String fullName,

    @NotNull(message = "EMPLOYEE_BRANCH_ID_REQUIRED")
    Long branchId,

    String jobTitle,
    LocalDate dateOfBirth,
    String avatar,
    Gender gender,
    UserStatus status,

    @NotEmpty(message = "EMPLOYEE_ROLES_REQUIRED")
    Set<Long> roles

) implements Serializable {
}
