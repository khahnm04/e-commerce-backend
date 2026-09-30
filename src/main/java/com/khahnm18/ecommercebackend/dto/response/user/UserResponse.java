package com.khahnm18.ecommercebackend.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.constant.user.GenderType;
import lombok.Builder;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(

    UUID id,
    UUID accountId,
    String email,
    String phoneNumber,
    String fullName,
    LocalDate dateOfBirth,
    String avatar,
    GenderType gender,
    Integer totalPoint,
    Integer usedPoint,
    AccountStatus status,
    Instant lastLoginAt,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
