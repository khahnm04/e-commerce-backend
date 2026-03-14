package com.khahnm04.ecommerce.modules.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeResponse(

    Long id,
    Long userId,
    String code,
    String fullName,
    String email,
    String phoneNumber,
    String jobTitle,
    LocalDate dateOfBirth,
    Gender gender,
    String avatar,
    UserStatus status,
    Set<String> roles,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt

) implements Serializable {
}
