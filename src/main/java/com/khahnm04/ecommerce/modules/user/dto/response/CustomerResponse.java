package com.khahnm04.ecommerce.modules.user.dto.response;

import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import com.khahnm04.ecommerce.common.dto.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse extends BaseResponse<Long> {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String avatarUrl;
    private UserStatus status;
    private Integer addressCount;

}
