package com.khahnm04.ecommerce.modules.auth.dto.request;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserUpdateRequest {

    @ValidEmail
    private String email;

    @ValidPhoneNumber
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    private LocalDate dateOfBirth;

    @NotBlank(message = "avatarUrl cannot be blank")
    private String avatarUrl;

    private Gender gender;

    private UserStatus status;

    @NotEmpty(message = "roles cannot be empty")
    private Set<Long> roles;

}
