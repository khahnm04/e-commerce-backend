package com.khahnm04.ecommerce.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.dto.response.user.EmployeeResponse;
import com.khahnm04.ecommerce.entity.auth.UserType;
import lombok.Builder;
import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminLoginResponse(

    EmployeeResponse employee,
    UserType userType,
    String accessToken,
    String refreshToken

) implements Serializable {
}
