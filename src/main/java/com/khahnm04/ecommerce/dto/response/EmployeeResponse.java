package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.EmployeeStatus;
import com.khahnm04.ecommerce.common.enums.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeResponse(

    Long id,
    String code,
    String fullName,
    String email,
    String phoneNumber,
    String jobTitle,
    LocalDate dateOfBirth,
    Gender gender,
    String avatar,
    EmployeeStatus status,
    Set<String> roles,
    Set<String> permissions,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy

) implements Serializable {
}
