package com.khahnm18.ecommercebackend.dto.response.user;

import com.khahnm18.ecommercebackend.enums.GenderType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.LocalDate;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MyProfileUserResponse(

    String email,
    String phoneNumber,
    String fullName,
    LocalDate dateOfBirth,
    String avatar,
    GenderType gender

) {}
