package com.khahnm18.ecommercebackend.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.Date;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse(

    String jwtId,
    String token,
    Date expiredTime

) {}
