package com.khahnm18.ecommercebackend.dto.request.user;

import com.khahnm18.ecommercebackend.enums.GenderType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record MyProfileAdminRequest(

    @NotBlank(message = "admin.email.required")
    String email,

    @NotBlank(message = "admin.phoneNumber.required")
    String phoneNumber,

    @NotBlank(message = "admin.jobTitle.required")
    String jobTitle,

    @NotBlank(message = "admin.fullName.required")
    String fullName,

    LocalDate dateOfBirth,
    String avatar,
    GenderType gender

) {}
