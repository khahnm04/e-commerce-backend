package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.CustomerStatus;
import com.khahnm04.ecommerce.common.enums.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponse(

        Long id,
        String fullName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        Gender gender,
        String avatar,
        CustomerStatus status,
        LocalDateTime lastLoginAt,
        Integer addressCount,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        List<String> providers

) implements Serializable {
}
