package com.khahnm18.ecommercebackend.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.constant.user.GenderType;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import lombok.Builder;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminResponse(

    UUID id,
    UUID accountId,
    String email,
    String phoneNumber,
    String jobTitle,
    String fullName,
    LocalDate dateOfBirth,
    String avatar,
    GenderType gender,
    AccountStatus status,
    List<RoleResponse> roles,
    Instant lastLoginAt,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
