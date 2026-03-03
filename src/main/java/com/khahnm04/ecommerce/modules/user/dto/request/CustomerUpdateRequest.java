package com.khahnm04.ecommerce.modules.user.dto.request;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequest implements Serializable {

    @ValidEmail
    private String email;

    @ValidPhoneNumber
    @NotBlank(message = "CUSTOMER_PHONE_NUMBER_REQUIRED")
    private String phoneNumber;

    @NotBlank(message = "CUSTOMER_FULL_NAME_REQUIRED")
    private String fullName;

    private LocalDate dateOfBirth;

    private String avatarUrl;

    private Gender gender;

    private UserStatus status;

}
