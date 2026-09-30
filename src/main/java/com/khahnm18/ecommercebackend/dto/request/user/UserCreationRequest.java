package com.khahnm18.ecommercebackend.dto.request.user;

import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.constant.user.GenderType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UserCreationRequest(

    @NotBlank(message = "user.email.required")
    String email,

    @NotBlank(message = "user.phoneNumber.required")
    String phoneNumber,

    @NotBlank(message = "user.password.required")
    String password,

    @NotBlank(message = "user.fullName.required")
    String fullName,

    LocalDate dateOfBirth,
    String avatar,
    GenderType gender,
    AccountStatus status

) {}
