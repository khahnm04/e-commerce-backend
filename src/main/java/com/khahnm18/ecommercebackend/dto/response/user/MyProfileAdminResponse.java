package com.khahnm18.ecommercebackend.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm18.ecommercebackend.enums.GenderType;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MyProfileAdminResponse(

    UUID accountId,
    String email,
    String phoneNumber,
    String jobTitle,
    String fullName,
    LocalDate dateOfBirth,
    String avatar,
    GenderType gender

) {}
