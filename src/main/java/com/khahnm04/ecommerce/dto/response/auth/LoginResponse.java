package com.khahnm04.ecommerce.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.dto.response.user.AccountUserResponse;
import com.khahnm04.ecommerce.dto.response.user.AccountAdminResponse;
import com.khahnm04.ecommerce.constant.auth.UserType;
import lombok.Builder;
import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponse(

    AccountAdminResponse accountAdmin,
    AccountUserResponse accountUser,
    UserType userType

) implements Serializable {
}
