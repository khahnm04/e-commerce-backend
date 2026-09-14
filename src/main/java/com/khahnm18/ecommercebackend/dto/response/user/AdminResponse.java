package com.khahnm18.ecommercebackend.dto.response.user;

import com.khahnm18.ecommercebackend.constant.user.GenderType;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import lombok.Builder;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
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
    List<RoleResponse> roles,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
