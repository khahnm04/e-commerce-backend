package com.khahnm18.ecommercebackend.dto.request.user;

import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.constant.user.GenderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record AdminUpdateRequest(

    @NotBlank(message = "admin.email.required")
    String email,

    @NotBlank(message = "admin.phoneNumber.required")
    String phoneNumber,

    @NotBlank(message = "admin.fullName.required")
    String fullName,

    @NotBlank(message = "admin.jobTitle.required")
    String jobTitle,

    LocalDate dateOfBirth,
    String avatar,
    GenderType gender,
    AccountStatus status,

    @NotEmpty(message = "admin.role.required")
    List<UUID> roles

) {}
