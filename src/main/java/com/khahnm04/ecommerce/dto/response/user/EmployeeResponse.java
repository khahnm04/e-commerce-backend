package com.khahnm04.ecommerce.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.entity.user.EmployeeStatus;
import com.khahnm04.ecommerce.entity.user.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeResponse(

    Long id,
    String code,
    String fullName,
    String jobTitle,
    String email,
    Instant dateOfBirth,
    String avatar,
    Gender gender,
    EmployeeStatus status,
    Instant lastLoginAt,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy,
    Instant deletedAt,
    String deletedBy

) implements Serializable {
}
