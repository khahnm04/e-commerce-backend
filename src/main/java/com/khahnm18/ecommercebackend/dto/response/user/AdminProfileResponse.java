package com.khahnm18.ecommercebackend.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm18.ecommercebackend.constant.user.GenderType;
import lombok.Builder;
import java.time.LocalDate;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminProfileResponse(

    String fullName,
    String jobTitle,
    String email,
    String phoneNumber,
    LocalDate dateOfBirth,
    String avatar,
    GenderType gender

) {}
