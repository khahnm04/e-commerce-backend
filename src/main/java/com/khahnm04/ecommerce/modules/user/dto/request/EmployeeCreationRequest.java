package com.khahnm04.ecommerce.modules.user.dto.request;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreationRequest implements Serializable {

    @ValidEmail
    private String email;

    @ValidPhoneNumber
    @NotBlank(message = "EMPLOYEE_PHONE_NUMBER_REQUIRED")
    private String phoneNumber;

    private String password;

    private String confirmPassword;

    @NotBlank(message = "EMPLOYEE_CODE_REQUIRED")
    private String code;

    @NotBlank(message = "EMPLOYEE_FULL_NAME_REQUIRED")
    private String fullName;

    @NotNull(message = "EMPLOYEE_BRANCH_ID_REQUIRED")
    private Long branchId;

    private String jobTitle;

    private LocalDate dateOfBirth;

    private String avatarUrl;

    private Gender gender;

    private UserStatus status;

    @NotEmpty(message = "EMPLOYEE_ROLES_REQUIRED")
    private Set<Long> roles;

}
