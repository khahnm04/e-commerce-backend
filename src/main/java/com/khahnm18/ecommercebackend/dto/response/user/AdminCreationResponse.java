package com.khahnm18.ecommercebackend.dto.response.user;

import com.khahnm18.ecommercebackend.constant.user.GenderType;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record AdminCreationResponse(

    UUID id,
    String email,
    String phoneNumber,
    String password,
    String jobTitle,
    String fullName,
    LocalDate dateOfBirth,
    String avatar,
    GenderType gender

) {}
