package com.khahnm18.ecommercebackend.dto.request.user;

import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.constant.user.GenderType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record AdminCreationRequest(

    @NotBlank(message = "admin.email.required")
    String email,

    @NotBlank(message = "admin.phoneNumber.required")
    String phoneNumber,

    @NotBlank(message = "admin.password.required")
    String password,

    @NotBlank(message = "admin.jobTitle.required")
    String jobTitle,

    @NotBlank(message = "admin.fullName.required")
    String fullName,

    LocalDate dateOfBirth,
    String avatar,
    GenderType gender,
    AccountStatus status

) {}
