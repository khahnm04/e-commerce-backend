package com.khahnm04.ecommerce.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.dto.response.auth.RoleResponse;
import com.khahnm04.ecommerce.constant.user.AccountAdminStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AccountAdminResponse(

    Long id,
    String fullName,
    String jobTitle,
    String email,
    String phoneNumber,
    Instant dateOfBirth,
    String avatar,
    Gender gender,
    AccountAdminStatus status,
    Instant lastLoginAt,
    Set<RoleResponse> roles,
    List<String> permissions,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy,
    Instant deletedAt,
    String deletedBy,
    Boolean isDeleted

) implements Serializable {
}
