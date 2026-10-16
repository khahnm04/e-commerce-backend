package com.khahnm18.ecommercebackend.dto.response.user;

import com.khahnm18.ecommercebackend.enums.GenderType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.LocalDate;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MyProfileAdminResponse(

    String email,
    String phoneNumber,
    String jobTitle,
    String fullName,
    LocalDate dateOfBirth,
    String avatar,
    GenderType gender

) {}
