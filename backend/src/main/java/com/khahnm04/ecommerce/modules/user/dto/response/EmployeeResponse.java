package com.khahnm04.ecommerce.modules.user.dto.response;

import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import com.khahnm04.ecommerce.common.dto.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse extends BaseResponse<Long> {

    private Long id;
    private Long userId;
    private String code;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String jobTitle;
    private String branch;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String avatarUrl;
    private UserStatus status;
    private Set<String> roles;

}
