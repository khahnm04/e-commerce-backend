package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import com.khahnm04.ecommerce.common.enums.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponse(

        Long id,
        Long userId,
        String fullName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        Gender gender,
        String avatar,
        UserStatus status,
        LocalDateTime lastLoginAt,
        Integer addressCount,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy

) implements Serializable {
}
