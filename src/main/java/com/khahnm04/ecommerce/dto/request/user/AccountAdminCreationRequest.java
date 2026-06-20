package com.khahnm04.ecommerce.dto.request.user;

import com.khahnm04.ecommerce.constant.user.AccountAdminStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import com.khahnm04.ecommerce.validation.email.ValidEmail;
import com.khahnm04.ecommerce.validation.password.ValidPassword;
import com.khahnm04.ecommerce.validation.phone.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Builder
public record AccountAdminCreationRequest(

    @NotBlank(message = "ACCOUNT_ADMIN_FULL_NAME_REQUIRED")
    String fullName,

    @NotBlank(message = "ACCOUNT_ADMIN_JOB_TITLE_REQUIRED")
    String jobTitle,

    @ValidEmail
    @NotBlank(message = "ACCOUNT_ADMIN_EMAIL_REQUIRED")
    String email,

    @ValidPhone
    @NotBlank(message = "ACCOUNT_ADMIN_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    @ValidPassword
    @NotBlank(message = "ACCOUNT_ADMIN_PASSWORD_REQUIRED")
    String password,

    Instant dateOfBirth,
    String avatar,
    Gender gender,
    AccountAdminStatus status,

    @NotEmpty(message = "ACCOUNT_ADMIN_ROLES_REQUIRED")
    Set<Long> roles

) implements Serializable {
}
