package com.khahnm04.ecommerce.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.constant.user.AccountUserStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AccountUserResponse(

    Long id,
    String fullName,
    String email,
    String phoneNumber,
    Instant dateOfBirth,
    String avatar,
    Gender gender,
    AccountUserStatus status,
    Instant lastLoginAt,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy,
    Instant deletedAt,
    String deletedBy

) implements Serializable {
}
